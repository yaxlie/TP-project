FROM tensorflow/tensorflow:1.12.0-devel-gpu-py3

RUN apt-get update && apt-get install -y \
  git 

RUN pip3 install --upgrade pip

COPY ./requirements.txt /requirements.txt
RUN pip install -r /requirements.txt

ARG UID
RUN useradd docker -l -u $UID -s /bin/bash -m
USER docker

WORKDIR /work

ENTRYPOINT ["/bin/bash"]
