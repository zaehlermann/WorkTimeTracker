TimeTracker
===========

### Time Tracking via RFID on Raspberry Pi

* This small project is designed to run as a standalone application on a Raspberry Pi.
* It is intended to provide a time tracking solution for smaller teams or companies where all team members work at one
  location and can log in and out locally using RFID tokens.
* It is designed to require minimal infrastructure to minimize maintenance costs. Essentially, only the power costs of
  the Raspberry Pi are accepted. No database, internet connection, or other cloud-based mumbo-jumbo is required.
* It is intended for teams that cannot or do not want to afford an IT administrator.
* The solution uses persistence based on CSV files, which can be saved and backed up from time to time (e.g., for
  monthly reporting) simply by downloading a file.

The solution contains out of two sub projects:

## Sub-projects

* [timetracker-rfid](timetracker-rfid/README.md): The RFID scanner application.
* [timetracker-ui](timetracker-ui/README.md):  The web interface for administrators.

## Documentation

* [TODOs](todos.md): Planned features and tasks.
* [Release Help](release.md): how to release.
