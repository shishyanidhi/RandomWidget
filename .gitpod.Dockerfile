FROM gitpod/workspace-full

RUN sudo apt-get update && sudo apt-get install -y wget unzip

# Install Android SDK
RUN mkdir -p /home/gitpod/android-sdk && \
    cd /home/gitpod/android-sdk && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O cmdtools.zip && \
    unzip cmdtools.zip -d cmdline-tools && \
    mkdir -p cmdline-tools/latest && \
    mv cmdline-tools/* cmdline-tools/latest/ && \
    rm cmdtools.zip

ENV ANDROID_HOME=/home/gitpod/android-sdk
ENV PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/build-tools
