import { Capability, Readiness, describe, expect, test } from "@teakit/test";
import type { ScenarioDefinition, ScenarioResult, TeaKitTestContext } from "@teakit/test";

describe.configure({
  timeout: "6m",
  capabilities: [
    Capability.LegacyJsonScenarios,
    Capability.ServerCommands,
    Capability.ClientScreen,
    Capability.ClientScreenshot,
  ],
});

describe("Kaf HUD", () => {
  test("opens the configuration screen", async ({ client, runtime, scenario }) => {
    await openPauseMenu(client, runtime);

    try {
      const result = await scenario.run(configUiDefinition as ScenarioDefinition, { timeoutMs: 240_000 });

      expect(failedSteps(result)).toEqual([]);
    } finally {
      await closeOpenScreens(client, runtime);
    }
  }, { readiness: [Readiness.ClientReady, Readiness.IntegratedServerReady, Readiness.PlayerSpawned] });

  test("renders the in-game overlay", async ({ client, runtime, scenario }) => {
    await closeOpenScreens(client, runtime);

    const result = await scenario.run(hudDefinition as ScenarioDefinition, { timeoutMs: 240_000 });

    expect(failedSteps(result)).toEqual([]);
  }, { readiness: [Readiness.ClientReady, Readiness.IntegratedServerReady, Readiness.PlayerSpawned] });
});

async function openPauseMenu(
  client: TeaKitTestContext["client"],
  runtime: TeaKitTestContext["runtime"],
) {
  for (let attempt = 0; attempt < 4; attempt++) {
    await client.key(256, { release: true, timeoutMs: 1000 }).catch(() => undefined);
    await runtime.wait(500, { timeoutMs: 1500 }).catch(() => undefined);

    const screen = await client.screen({ timeoutMs: 1000 }).catch(() => null);
    if (screen?.title === "Game Menu" || screen?.id === "pause") {
      return;
    }
  }

  throw new Error("Failed to open the pause menu before the config UI scenario");
}

async function closeOpenScreens(
  client: TeaKitTestContext["client"],
  runtime: TeaKitTestContext["runtime"],
) {
  for (let attempt = 0; attempt < 4; attempt++) {
    const screen = await client.screen({ timeoutMs: 1000 }).catch(() => null);
    if (screen == null || (screen.id == null && screen.title == null)) {
      return;
    }

    await client.key(256, { release: true, timeoutMs: 1000 }).catch(() => undefined);
    await runtime.wait(250, { timeoutMs: 1250 }).catch(() => undefined);
  }
}

function failedSteps(result: ScenarioResult): string[] {
  return ["setup", "steps", "cleanup"].flatMap((phase) => {
    const phaseResults = result[phase];
    if (!Array.isArray(phaseResults)) {
      return [];
    }

    return phaseResults
      .filter((step) => {
        const stepResult = step.result as Record<string, unknown> | undefined;
        return stepResult?.failure != null || stepResult?.failed === true;
      })
      .map((step) => `${phase}[${step.index ?? "?"}] ${step.action ?? "unknown"}`);
  });
}

const configUiDefinition = {
  name: "kafhud-config-ui-fabric",
  steps: [
    { action: "wait_ms", durationMs: 3500 },
    { action: "activate_widget", label: "Mods", contains: true, waitAfterMs: 800 },
    { action: "wait_for_screen", title: "Mods", timeoutMs: 5000, pollMs: 100 },
    { action: "click_list_entry", label: "KafHUD", contains: false, nth: 0, listRole: "mod_list", waitAfterMs: 300 },
    {
      action: "activate_widget_class",
      widgetClass: "com.terraformersmc.modmenu.gui.widget.LegacyTexturedButtonWidget",
      nth: 1,
      waitAfterMs: 800,
    },
    {
      action: "wait_for_screen",
      screenClass: "com.iamkaf.konfig.impl.v1.KonfigConfigScreen",
      timeoutMs: 5000,
      pollMs: 100,
    },
    { action: "wait_for_list_entry", label: "General", contains: false, timeoutMs: 5000, pollMs: 100 },
    { action: "wait_for_list_entry", label: "Enable HUD", contains: false, timeoutMs: 5000, pollMs: 100 },
    { action: "wait_for_list_entry", label: "Background Mode", contains: false, timeoutMs: 5000, pollMs: 100 },
    { action: "wait_for_list_entry", label: "Coordinates", contains: false, timeoutMs: 5000, pollMs: 100 },
    {
      action: "screenshot",
      name: "kafhud-config-ui",
      hideOverlay: true,
      hideDecoration: true,
      hideWindowDecoration: true,
    },
  ],
  cleanup: [],
} as ScenarioDefinition;

const hudDefinition = {
  name: "kafhud-hud",
  setup: [
    { action: "command", command: "/gamerule doDaylightCycle false" },
    { action: "command", command: "/time set 96000" },
    { action: "command", command: "/weather clear" },
    { action: "command", command: "/fill 10 179 -37 15 179 -32 minecraft:glass replace" },
    { action: "command", command: "/fill 10 180 -37 15 183 -32 minecraft:air replace" },
    { action: "command", command: "/tp @s 12.5 180 -34.5 90 0" },
    { action: "wait_ms", durationMs: 1000 },
  ],
  steps: [
    { action: "assert_player_position", x: 12.5, y: 180.0, z: -34.5, tolerance: 0.25 },
    { action: "assert_player_rotation", yaw: 90.0, pitch: 0.0, tolerance: 0.5 },
    { action: "screenshot", name: "kafhud-overlay-day-4", hideOverlay: true },
  ],
  cleanup: [
    { action: "command", command: "/gamerule doDaylightCycle true" },
    { action: "command", command: "/fill 10 179 -37 15 183 -32 minecraft:air replace" },
  ],
} as ScenarioDefinition;
