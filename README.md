# Centralized-Mutual-Exclusion-Traffic-System

This project implements a distributed traffic light controller using a central coordinator and two traffic light nodes. The system ensures that only one traffic light can display a green light at a time, simulating real-world traffic management.

## Overview
The project consists of:

- Coordinator: Manages the allocation of a token to traffic light nodes.
- Traffic Light Nodes: Each node controls a traffic light and requests the token from the coordinator to operate.

## Basic Functionality
Coordinator: Listens for requests from traffic light nodes and grants tokens to control the green light.
Traffic Light Nodes:
Request the token from the coordinator.
Execute a critical section (simulated by displaying "Green light ON" for a few seconds).
Return the token to the coordinator after completing the critical section.

## Notes
Ensure ports 7000 and 7001 are available and not in use by other applications.
Handle exceptions gracefully and log activities for debugging purposes.

## License
This project is licensed under the [MIT License](./LICENSE.txt).

## Contact
For any questions, feedback, or collaborations, feel free to reach out:
- Email: uzairahmedrak@gmail.com
