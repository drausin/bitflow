# Docker

We use Docker containers for isolation (see [this post](http://www.ybrikman.com/writing/2015/05/19/docker-osx-dev/) for a nice discussion why). Follow these steps to set up Docker on your Mac:

First you need to install VirtualBox

    brew tap caskroom/cask
    brew install brew-cask
    brew cask install virtualbox --appdir=/Applications

The easiest way to install Docker on a Mac is via `homebrew` via

    brew install docker docker-machine docker-compose

# Python

We use python3 (currently 3.5.0). 

1. (if necessary) Download and run the python3 installer from [here](https://www.python.org/downloads/).
2. Upgrade virtualenv per [this](http://stackoverflow.com/questions/23842713/using-python-3-in-virtualenv) SO post.
        
        sudo pip install --upgrade virtualenv

3. Create a virtual environment for our `bitflow` project.

        mkvirtualenv --no-site-packages -p /usr/local/bin/python3 bitflow

4. Install the AWS CLI (while in the `bitflow` virtualenv).

        pip install awscli

5. Configure the AWS CLI. You should have the AWS Access Key ID and Secret Access Key stored somewhere secure. We use region `us-east-1` and output format 'json'.

        aws configure

6. Add tab completion to the virtualenv preactivate hook.

        echo "complete -C '$(which aws_completer)' aws" >> /Users/dwulsin/.virtualenvs/bitflow/bin/preactivate




