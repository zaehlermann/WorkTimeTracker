Timetracker UI
=====================
This is the web application to manage employees, rfids and initiate the time journal generation

- [ ] TODO Replace or update this README with instructions relevant to your application

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

## Getting Started

The [Getting Started](https://vaadin.com/docs/latest/getting-started) guide will quickly familiarize you with your new
Timetracker Ui implementation. You'll learn how to set up your development environment, understand the project
structure, and find resources to help you add muscles to your skeleton — transforming it into a fully-featured
application.

## Disabling the Login Screen

By default, all views in the project are restricted to *authenticated users*. This means that if you try to access any
view without being logged in, you'll be redirected to the login screen.

To make views publicly accessible, replace the `@PermitAll` annotation with `@AnonymousAllowed` from the
`com.vaadin.flow.server.auth` package. For example, applying this change to `MainView` allows you to open
http://localhost:8080 without logging in.

If you allow anonymous access to `TaskListView`, you’ll also need to update the method-level security annotations in
`TaskService`. Without these changes, the view may load, but data fetching or saving will fail due to backend access
restrictions.

## Removing Security

This project includes a basic, preconfigured security setup. If you prefer to implement your own security configuration
from scratch, delete the `security` Java package.

After this change, you may encounter some minor compilation errors. These typically occur
in places where the current security setup is referenced - for example, integration tests that run as specific users,
or UI components (like the main layout) that display the current user's name and avatar.

To resolve these issues, either remove the affected code or refactor it to align with your custom security setup.
