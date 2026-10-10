# McSkinExtractor

McSkinExtractor is a small NeoForge client mod that exports the skins saved by the Minecraft Launcher into regular PNG files.

## How To Use

1. Download or build the mod `.jar`.
2. Put the `.jar` in your Minecraft `mods` folder.
3. Launch Minecraft with the matching NeoForge profile.
4. Open **Options** -> **Skin Customization**.
5. Click **Export Skins**.

After export, a new `skins` folder will appear in your `.minecraft` directory. Each exported skin gets its own folder containing:

- the skin PNG
- the model preview PNG
- an `info.txt` file with basic skin details

## Notes

- This reads `launcher_skins.json` from your Minecraft game directory.
- Exported files are written locally only.
- Existing exported skins may be overwritten when you export again.
