How-to release
=================

Feature Release, e.g. for v.1.1.0

# Create a branch

    mvn release:branch -DbranchName="bugfix/timetracker-1.1"
    git checkout bugfix/timetracker-1.1

# Perform QA etc etc

* Check for SNAPSHOT dependencies.
* Prepare next target version number.

# Create tag

    mvn release:prepare
    mvn release:clean

There is no need for mvn release:perform yet.

# Switch back to master

At this point you can switch back to trunk by executing:

    git checkout master
