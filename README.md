# SonarQube CNES Scan Plugin [![Build Status](https://travis-ci.org/lequal/sonar-cnes-scan-plugin.svg?branch=master)](https://travis-ci.org/lequal/sonar-cnes-scan-plugin) [![Quality Gate](https://sonarcloud.io/api/badges/gate?key=fr.cnes.sonar.plugins.scan%3Asonar-cnes-scan-plugin)](https://sonarcloud.io/dashboard?id=fr.cnes.sonar.plugins.scan%3Asonar-cnes-scan-plugin)
SonarQube is an open platform to manage code quality. This plugin adds ability to launch analysis directly from the web interface and export reports.

This plugin is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.

### Quickstart
- Setup a SonarQube instance
- Install the plugin
- Log in SonarQube
- Go to More > CNES Analysis
- Run an analysis

### Features
- Run analysis from web interface
- Export data as OpenXML reports

### Configuration
- Sign-in as an administrator
- Go to Administration > Sonar CNES Scan plugin
  - Timeout : Set the maximum time to realize the analysis
  - Pylintrc : Set the folder containing all pylintrc files.
  - Reports : Set reports generator's path, templates, and destination folder.
  - Workspace :  Set the folder containing projects to analyze.

### Resources
- Run an analysis
- Architecture

### How to contribute
If you experienced a problem with the plugin please open an issue. Inside this issue please explain us how to reproduce this issue and paste the log.

If you want to do a PR, please put inside of it the reason of this pull request. If this pull request fix an issue please insert the number of the issue or explain inside of the PR how to reproduce this issue.

### License
Copyright 2018 LEQUAL.

Licensed under the [GNU General Public License, Version 3.0](https://www.gnu.org/licenses/gpl.txt)
