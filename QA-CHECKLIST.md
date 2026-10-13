# BPO Intelligence Hub 12.1.0 — QA checklist

## Automated checks
- `node --check web/app.js`
- Web `index.html`, `app.js`, `styles.css`, icon, manifest and bundled Excel template are mirrored into Android assets by GitHub Actions.
- Build workflow checks the source syntax, product version (`12.1.0`) and Android build code (`31`) before compiling.
- Excel template has headers only and contains no demo/example records.

## Manual smoke test after installing the APK
1. Open the app with an empty local database: dashboard counts should be zero; 3-way search should show guidance, not a list of fake records.
2. Open/close the mobile menu; tap every navigation item; use Back from a detail/form and from a subpage.
3. Add a project, client, benchmark and lead. Edit and delete one record of each type; reopen the app and verify saved records remain.
4. Change each filter: result rows and count should update immediately; type in global search and verify current filters remain selected and the page does not jump to the top.
5. Search using each of the 3-way fields and open a result to verify it points to the correct source record.
6. Select all five themes, customize accent color, choose font size and verify settings persist after relaunch.
7. Download the blank template and confirm all four sheets contain column headers only. Import a filled template and the supplied five-column structure; confirm preview, duplicate handling and record counts.
8. Export `.xlsx`, ordinary JSON backup and encrypted `.bpoenc`; verify files exist in the selected folder. Restore JSON and encrypted backup with the right password; wrong password and damaged file should fail without replacing current data.
9. Test with airplane mode enabled: core screens and CSV import/export should remain available; `.xlsx` import requires the Excel library or CSV fallback.
