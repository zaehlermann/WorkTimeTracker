Timetracker UI
=====================
This is the web application to manage employees, RFIDs and initiate the time journal generation

# Building

To start the application in development mode, import it into your IDE and run the `Application` class.
You can also start the application from the command line by running:

```bash
./mvnw
```

To build the application in production mode, run:

```bash
./mvnw -Pproduction package
```

Start the application with:

```bash
java -jar target/timetracker-ui-1.1.0.jar
```

To also build a Docker image, continue by running:

```bash
docker build -t my-application:latest .
```

# Deploy to Raspberry Pi

My raspberry pi has the dnc timetracker.local. The UI service must run under the same user as the timetracker-rfid service.

1. Transfer jar file and autostart service file to the raspberry pi

```bash
scp target/timetracker-ui-1.1.0.jar mike@timetracker.local:/home/mike/timetracker
scp timetracker-ui.service mike@timetracker.local:/home/mike/timetracker
```

2. Login by ssh and copy the auto start service file timetracker-ui.service to /etc/systemd/system

```bash
ssh mike@timetracker.local
cd timetracker
mv timetracker-ui-1.1.0 timetracker-ui.jar
cp timetracker-ui.service /home/mike/.config/systemd/user
```

3. Enable and start the service:

```bash
systemctl --user daemon-reload
systemctl --user enable timetracker-ui
systemctl --user start timetracker-ui
```

4. Check the logs

```bash
systemctl status timetracker-ui
watch -n 1 systemctl --user status timetracker-ui
```