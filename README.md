# Trading Gate Android

Aplikasi Android sederhana untuk menjadi gerbang disiplin sebelum membuka MetaTrader 5.

Alur:
1. Awal terkunci.
2. Checklist Liquidity, CISD, POI, dan SL/TP/RR harus dicentang.
3. Setelah lengkap, MT5 dibuka selama 15 menit.
4. Setelah 15 menit, gate terkunci kembali dan checklist di-reset.

## Build lewat GitHub Actions
Push project ini ke repository GitHub, lalu buka **Actions → Build Trading Gate APK**.
Artifact `TradingGate-debug-apk` berisi `app-debug.apk`.

Catatan: aplikasi ini tidak dapat memblokir MT5 secara sistem-level setelah pengguna membuka MT5 melalui cara lain. Fungsi yang disediakan di MVP ini adalah gerbang disiplin/checklist dan tombol pembuka MT5.
