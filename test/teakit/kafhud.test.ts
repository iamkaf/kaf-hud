import { Capability, Readiness, describe, expect, pos, test } from "@teakit/test";
import type { ClientScreen, TeaKitTestContext } from "@teakit/test";

describe.configure({
  timeout: "6m",
  readiness: [Readiness.World, Readiness.Player],
  capabilities: [
    Capability.ClientInput,
    Capability.ClientScreen,
    Capability.ClientScreens,
    Capability.ClientScreenshot,
    Capability.PlayerSelf,
    Capability.RuntimeTiming,
    Capability.ServerCommands,
  ],
});

describe("Kaf HUD", () => {
  test("opens the configuration screen", async (ctx) => {
    const health = await ctx.runtime.health();
    if (health.loader !== "fabric") return;

    await openPauseMenu(ctx);
    try {
      let screen = await ctx.client.screen();
      await screen.widgets().activate({ label: "Mods", contains: true });
      screen = await ctx.client.waitForScreen("Mods", { timeoutMs: 5_000 });
      await screen.lists("mod_list").entry({ label: "KafHUD", nth: 0 }).activate();
      await ctx.runtime.wait(300);
      screen = await ctx.client.screen();
      await activateConfigure(screen);
      await ctx.runtime.wait(800);

      screen = await waitForEntry(ctx, "General");
      for (const label of ["Enable HUD", "Background Mode", "Background Color", "Coordinates"]) {
        expect(screen.lists().entries().map((entry) => entry.label)).toContain(label);
      }

      for (const labels of [
        ["Show Direction", "Direction Display Mode"],
        ["Coordinates Color", "North Direction Color"],
        ["Biome", "Biome Color"],
        ["Day Counter", "Day Counter Color"],
      ]) {
        screen = await screen.scroll({ vertical: -8 });
        await ctx.runtime.wait(250);
        for (const label of labels) await waitForEntry(ctx, label);
      }
      await ctx.client.screenshot("kafhud-config-ui");
    } finally {
      await ctx.client.closeMenus();
    }
  });

  test("renders the in-game overlay", async (ctx) => {
    await ctx.client.closeMenus();
    await ctx.commands.batch([
      "/gamerule doDaylightCycle false",
      "/time set 96000",
      "/weather clear",
      "/fill 10 179 -37 15 179 -32 minecraft:glass replace",
      "/fill 10 180 -37 15 183 -32 minecraft:air replace",
      "/tp @s 12.5 180 -34.5 90 0",
    ]);
    await ctx.runtime.wait(1000);

    try {
      const pose = await ctx.player.pose();
      expect(pose.position).toBeNear(pos(12.5, 180, -34.5), { distance: 0.25 });
      expect(pose.yaw).toBeGreaterThanOrEqual(89.5);
      expect(pose.yaw).toBeLessThanOrEqual(90.5);
      expect(pose.pitch).toBeGreaterThanOrEqual(-0.5);
      expect(pose.pitch).toBeLessThanOrEqual(0.5);
      await ctx.client.screenshot("kafhud-overlay-day-4");
    } finally {
      await ctx.commands.batch([
        "/gamerule doDaylightCycle true",
        "/fill 10 179 -37 15 183 -32 minecraft:air replace",
      ]);
    }
  });
});

async function openPauseMenu(ctx: TeaKitTestContext) {
  for (let attempt = 0; attempt < 4; attempt += 1) {
    await ctx.client.key(256, { release: true }).catch(() => undefined);
    await ctx.runtime.wait(500).catch(() => undefined);
    const screen = await ctx.client.screen().catch(() => null);
    if (screen?.title === "Game Menu" || screen?.id === "pause") return;
  }
  throw new Error("Failed to open the pause menu before the Kaf HUD config test");
}

async function activateConfigure(screen: ClientScreen) {
  const selectors = [
    { widgetClass: "com.terraformersmc.modmenu.gui.widget.LegacyTexturedButtonWidget", nth: 1 },
    { label: "Configure...", nth: 0 },
    { label: "Config", nth: 0 },
  ];
  for (const selector of selectors) {
    try {
      await screen.widgets().activate(selector);
      return;
    } catch {
      // Try the next semantic/compat selector.
    }
  }
  throw new Error("KafHUD configuration button was not available");
}

async function waitForEntry(ctx: TeaKitTestContext, label: string): Promise<ClientScreen> {
  const startedAt = Date.now();
  while (Date.now() - startedAt < 10_000) {
    const screen = await ctx.client.screen();
    if (screen.lists().entries().some((entry) => entry.label === label)) return screen;
    await ctx.runtime.wait(100);
  }
  throw new Error(`Timed out waiting for Kaf HUD config entry: ${label}`);
}
