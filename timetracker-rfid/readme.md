Time Tracker RFI
================
This application records the rfid scans.

# Deploy to Raspberry Pi

My raspberry pi has the dnc timetracker.local

1. Transfer jar file and autostart service file to the raspberry pi

```bash
scp target/timetracker-rfid-1.0-SNAPSHOT.jar mike@timetracker.local:/home/mike/timetracker
scp timetracker-rfid.service mike@timetracker.local:/home/mike/timetracker
```

2. Login by ssh and copy the auto start service file timetracker-rfid.service to /etc/systemd/system

```bash
ssh mike@timetracker.local
cd timetracker
mv timetracker-rfid-1.0-SNAPSHOT.jar timetracker-rfid.jar
sudo cp timetracker-rfid.service /etc/systemd/system
```

3. Enable and start the service:

```bash
sudo systemctl daemon-reload
sudo systemctl enable timetracker-rfid
sudo systemctl start timetracker-rfid
sudo systemctl status timetracker-rfid
watch -n 2 sudo systemctl status timetracker-rfid
```