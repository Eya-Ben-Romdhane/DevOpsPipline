FROM ubuntu:latest
LABEL authors="nblenovo"

ENTRYPOINT ["top", "-b"]