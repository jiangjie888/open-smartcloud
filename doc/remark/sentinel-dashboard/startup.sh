#!/bin/bash
java -Dserver.port=8801 -Dcsp.sentinel.dashboard.server=localhost:8801 -Dproject.name=sentinel-dashboard -Dcsp.sentinel.api.port=8802 -jar sentinel-dashboard-1.6.0.jar