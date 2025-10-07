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

2. Enable autostart
On theory the autostart should be started by systemd --user 
* when my user mike logs in. 
* auto login is enabled for the user mike.
* linger is enabled for the user mike.
* service is has StandardInput=tty
* TTYPath=/dev/tty1 (the auto login console)
However it was not working, and I switched to the hacky variant and added the service start to the .bashrc file. 
* ```java -jar /home/mike/timetracker/timetracker-rfid.jar```

If someone whant to debug this, here is the steps I did:

2.1 Login by ssh and copy the auto start service file timetracker-rfid.service to /home/mike/.config/systemd/user
```bash
ssh mike@timetracker.local
cd timetracker
mv timetracker-rfid-1.0-SNAPSHOT.jar timetracker-rfid.jar
sudo cp timetracker-rfid.service /home/mike/.config/systemd/user
```
2.2 Enable auto login for the user mike by sudo raspi-config
2.3 Enable and start the service
```bash
systemctl --user daemon-reload
systemctl --user enable timetracker-rfid
systemctl --user start timetracker-rfid
systemctl --user status timetracker-rfid
sudo loginctl enable-linger mike
```

4. Check the logs

```bash
watch -n 0.1 systemctl --user status timetracker-rfid 
journalctl --user -u timetracker-rfid.service -f
```
