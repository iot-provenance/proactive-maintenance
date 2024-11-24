# RPL Border Router with Contiki-NG and Docker

This repository provides an example implementation of a border router using **Contiki-NG** and Docker. It demonstrates how to set up and use the `rpl-border-router` example to enable external access to IoT nodes.

## Prerequisites

Before you begin, ensure you have the following installed on your system:

- [Contiki-NG](https://github.com/contiki-ng/contiki-ng) (latest version)
- [Docker](https://www.docker.com/)
- GNU Make (for building Contiki-NG projects)

## Project Structure


## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/rpl-border-router.git
cd rpl-border-router

### 2. Build the Border Router Firmware
Navigate to the border-router directory and compile the Contiki-NG rpl-border-router example:

bash
""
cd border-router
make clean
make TARGET=cooja BOARD=native
This creates a native border router binary for testing on your local machine.

3. Start the Docker Container
A Docker container is used to manage the network interface and provide external access. Use the provided docker-compose.yml file to start the container:

bash
""
docker-compose up -d
This will expose the border router to the external network.

4. Run the Border Router
Start the border router on your machine:

bash
""
cd border-router
sudo ./border-router.native
Once the router is running, it will listen for incoming connections from nodes.

5. Add IoT Nodes
Compile the firmware for IoT nodes and upload it to your devices or run it in a Contiki-NG simulator:

bash
""
cd nodes/node-firmware
make clean
make TARGET=cooja
Alternatively, you can use precompiled node firmware available in the nodes directory.

6. Verify Connectivity
After the setup, you can test connectivity by accessing the nodes via the border router. Use the following command to check the routing table:

bash
""
curl http://[border-router-ip]/routes
Replace [border-router-ip] with the IPv6 address of the border router.

7. Interact with Nodes
Send commands or retrieve data from the nodes via the border router. Example:

bash
""
curl http://[node-ip]/sensor-data
8. Stop the Docker Container
To stop and remove the Docker container:

bash
""
docker-compose down
Configuration
Border Router Configuration
Modify the project-conf.h file in the border-router directory to customize the border router settings, such as PAN ID, channel, and IPv6 prefix.

Docker Configuration
Edit the docker-compose.yml file to change container settings or network configuration.

Troubleshooting
Common Issues
Border Router Not Accessible

Ensure Docker is running and the container is properly configured.
Verify that the border router firmware was compiled successfully.
Nodes Not Joining the Network

Check the node firmware configuration (e.g., PAN ID and channel).
Ensure the border router is running and listening for incoming connections.
Logs
Use the following command to view Docker container logs:

bash
""
docker logs [container-name]
Replace [container-name] with the name of your Docker container.

Contributing
Contributions are welcome! Feel free to open an issue or submit a pull request to improve this project.

License
This project is licensed under the MIT License. See the LICENSE file for more details.

css
""

Bu `README.md`, GitHub'da yayınlamanız için genel ve düzenli bir belge yapısı sunar. İhtiyaçlarınıza 
