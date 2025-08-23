Timetracker UI
=====================
This is the web application to manage employees, rfids and initiate the time journal generation

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
java -jar target/timetracker-ui-1.0.0.jar
```

To also build a Docker image, continue by running:

```bash
docker build -t my-application:latest .
```

# Deploy to Raspberry Pi

My raspberry pi has the dnc timetrcker.local

```bash
scp target/timetracker-ui-1.0-SNAPSHOT.jar mike@timetracker.local:/home/mike/timetracker
```

copy the auto start service file timetracker.service to /etc/systemd/system
Enable and start the service:

```bash
sudo systemctl daemon-reload
sudo systemctl enable timetracker
sudo systemctl start timetracker
sudo systemctl status timetracker
watch -n 2 sudo systemctl status timetracker
```