# Use a minimal base image
FROM debian:bullseye-slim

# Set environment variables to avoid interactive prompts during package installation
ENV DEBIAN_FRONTEND=noninteractive

# Install necessary packages
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    build-essential \
    curl \
    git \
    ca-certificates \
    perl \
    python3 \
    python3-pip \
    libelf-dev \
    libdw-dev \
    binutils-dev \
    pkg-config \
    elfutils \
    universal-ctags \
    gcc \
    g++ \
    && apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install Vtable Dumper
RUN git clone https://github.com/lvc/vtable-dumper.git /opt/vtable-dumper && \
    cd /opt/vtable-dumper && \
    make && \
    make install prefix=/usr

# Install abi-dumper
RUN git clone https://github.com/lvc/abi-dumper.git /opt/abi-dumper && \
    cd /opt/abi-dumper && \
    make install prefix=/usr && \
    ln -s /usr/share/abi-dumper/abi-dumper.pl /usr/local/bin/abi-dumper

# Install abi-compliance-checker
RUN git clone https://github.com/lvc/abi-compliance-checker.git /opt/abi-compliance-checker && \
    cd /opt/abi-compliance-checker && \
    make install prefix=/usr && \
    ln -s /usr/share/abi-compliance-checker/abi-compliance-checker.pl /usr/local/bin/abi-compliance-checker

# Set the entrypoint to bash for interactive use
ENTRYPOINT ["/bin/bash"]

