Time Tracker UI - update me on Raspberry
=======================================

# Building

* Build the application in production mode

```bash
mvn -Pproduction package
```

# Backup the recorded files

Login first to the existing service and do a backup of the files.

1. Transfer jar file to the raspberry pi

```bash
scp target/timetracker-ui-1.4.0.jar mike@timetracker.local:/home/mike/timetracker
or
scp target/timetracker-ui-1.4.0.jar mike@192.168.188.83:/home/mike/timetracker
```

2.

* Stop the service.
* Rename the old service jar for backup.
* Rename the new service jar to run as service.
* Start the service.

```bash
ssh mike@timetracker.local
cd timetracker
systemctl --user stop timetracker-ui
mv timetracker-ui.jar timetracker-ui-1.2.0-backup.jar
mv timetracker-ui-1.4.0.jar timetracker-ui.jar
systemctl --user start timetracker-ui
```

4. Check the logs

```bash
systemctl status timetracker-ui
watch -n 1 systemctl --user status timetracker-ui
```