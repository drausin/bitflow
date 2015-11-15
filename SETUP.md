# Docker

We use Docker containers for isolation (see [this post](http://www.ybrikman.com/writing/2015/05/19/docker-osx-dev/) for a nice discussion why). Follow these steps to set up Docker on your Mac:
1. Intall the Docker Toolbox via the instructions [here](https://docs.docker.com/mac/step_one/)
2. As a precaution against [this issue (see mdub's comment)](https://github.com/docker/machine/issues/1031), open your ~/.ssh/config and remove the "ControlMaster" block, and also kill all currently open ssh sessions with `ps aux | grep ssh | awk '{print $2}' | xargs kill`. You can then try starting the docker instance again.
3. Set up the VirtualBox VM (called "default") that Docker will use.

        dwulsin@Carthage : bitcoin-flow $ bash '/Applications/Docker/Docker Quickstart Terminal.app/Contents/Resources/Scripts/start.sh'
        Creating Machine default...
        Creating VirtualBox VM...
        Creating SSH key...
        Starting VirtualBox VM...
        Starting VM...
        To see how to connect Docker to this machine, run: docker-machine env default
        Starting machine default...
        Started machines may have new IP addresses. You may need to re-run the `docker-machine env` command.
        Setting environment variables for machine default...

                                ##         .
                          ## ## ##        ==
                       ## ## ## ## ##    ===
                   /"""""""""""""""""\___/ ===
              ~~~ {~~ ~~~~ ~~~ ~~~~ ~~~ ~ /  ===- ~~~
                   \______ o           __/
                     \    \         __/
                      \____\_______/

        docker is configured to use the default machine with IP 192.168.99.101
        For help getting started, check out the docs at https://docs.docker.com

        dwulsin@Carthage : bitcoin-flow $ docker-machine status default
        Running

4. We can ssh to to the docker host VM.

        dwulsin@Carthage : bitcoin-flow $ docker-machine ssh default

                                ##         .
                          ## ## ##        ==
                       ## ## ## ## ##    ===
                   /"""""""""""""""""\___/ ===
              ~~~ {~~ ~~~~ ~~~ ~~~~ ~~~ ~ /  ===- ~~~
                   \______ o           __/
                     \    \         __/
                      \____\_______/
         _                 _   ____     _            _
        | |__   ___   ___ | |_|___ \ __| | ___   ___| | _____ _ __
        | '_ \ / _ \ / _ \| __| __) / _` |/ _ \ / __| |/ / _ \ '__|
        | |_) | (_) | (_) | |_ / __/ (_| | (_) | (__|   <  __/ |
        |_.__/ \___/ \___/ \__|_____\__,_|\___/ \___|_|\_\___|_|
        Boot2Docker version 1.8.2, build master : aba6192 - Thu Sep 10 20:58:17 UTC 2015
        Docker version 1.8.2, build 0a8c2e3
        docker@default:~$
        docker@default:~$ exit
        dwulsin@Carthage : bitcoin-flow $

5. Run `eval "$(docker-machine env default)"` to load environment settings for the docker host that is running.
6. Run `docker ps` which should return successfully - docker is now up and running on your mac!
7. Make sure things are working as expected.

        dwulsin@Carthage : bitcoin-flow $ docker run hello-world
        Unable to find image 'hello-world:latest' locally
        latest: Pulling from library/hello-world

        535020c3e8ad: Pull complete
        af340544ed62: Pull complete
        Digest: sha256:a68868bfe696c00866942e8f5ca39e3e31b79c1e50feaee4ce5e28df2f051d5c
        Status: Downloaded newer image for hello-world:latest

        Hello from Docker.
        This message shows that your installation appears to be working correctly.

        To generate this message, Docker took the following steps:
         1. The Docker client contacted the Docker daemon.
         2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
         3. The Docker daemon created a new container from that image which runs the
            executable that produces the output you are currently reading.
         4. The Docker daemon streamed that output to the Docker client, which sent it
            to your terminal.

        To try something more ambitious, you can run an Ubuntu container with:
         $ docker run -it ubuntu bash

        Share images, automate workflows, and more with a free Docker Hub account:
         https://hub.docker.com

        For more examples and ideas, visit:
         https://docs.docker.com/userguide/

8. If you are new to Docker, follow steps [two](https://docs.docker.com/mac/step_two/), [three](https://docs.docker.com/mac/step_three/), and [four](https://docs.docker.com/mac/step_four/) to play around with some example images. [This page](https://docs.docker.com/userguide/dockerimages/) is a good high-level summary of Docker images.

# Python

We use python3 (currently 3.5.0). 

1. (if necessary) Download and run the python3 installer from [here](https://www.python.org/downloads/).
2. Upgrade virtualenv per [this](http://stackoverflow.com/questions/23842713/using-python-3-in-virtualenv) SO post.
        
        sudo pip install --upgrade virtualenv

3. Create a virtual environment for our `bitcoin-flow` project.

        mkvirtualenv --no-site-packages -p /usr/local/bin/python3 bitcoin-flow

4. Install the AWS CLI (while in the `bitcoin-flow` virtualenv).

        pip install awscli

5. Configure the AWS CLI. You should have the AWS Access Key ID and Secret Access Key stored somewhere secure. We use region `us-east-1` and output format 'json'.

        aws configure

6. Add tab completion to the virtualenv preactivate hook.

        echo "complete -C '$(which aws_completer)' aws" >> /Users/dwulsin/.virtualenvs/bitcoin-flow/bin/preactivate




