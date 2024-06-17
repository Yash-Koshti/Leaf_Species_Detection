# Use an official Python runtime as a parent image
FROM python:3.10-slim

ENV DEBIAN_FRONTEND=noninteractive

# Set the working directory
WORKDIR /app

# Install dependencies
RUN apt-get update && \
    apt-get install -y libpq-dev gcc g++ && \
    apt-get clean

# Install OpenCV dependencies and OpenCV itself
RUN apt-get update && \
    apt-get install -y \
    libopencv-dev \
    build-essential \
    cmake \
    git \
    pkg-config \
    libjpeg-dev \
    libpng-dev \
    libtiff-dev \
    libavcodec-dev \
    libavformat-dev \
    libswscale-dev \
    libv4l-dev \
    libxvidcore-dev \
    libx264-dev \
    libgtk2.0-dev \
    libatlas-base-dev \
    gfortran \
    python3-dev \
    libgl1-mesa-glx && \
    apt-get clean

# Create symlinks for OpenCV libraries to match expected versions
RUN ln -s /usr/lib/x86_64-linux-gnu/libopencv_highgui.so.4.6.0 /usr/lib/x86_64-linux-gnu/libopencv_highgui.so.4.5d && \
    ln -s /usr/lib/x86_64-linux-gnu/libopencv_videoio.so.4.6.0 /usr/lib/x86_64-linux-gnu/libopencv_videoio.so.4.5d && \
    ln -s /usr/lib/x86_64-linux-gnu/libopencv_imgcodecs.so.4.6.0 /usr/lib/x86_64-linux-gnu/libopencv_imgcodecs.so.4.5d && \
    ln -s /usr/lib/x86_64-linux-gnu/libopencv_core.so.4.6.0 /usr/lib/x86_64-linux-gnu/libopencv_core.so.4.5d && \
    ln -s /usr/lib/x86_64-linux-gnu/libopencv_imgproc.so.4.6.0 /usr/lib/x86_64-linux-gnu/libopencv_imgproc.so.4.5d

# Check if the symlinks were created correctly
RUN ls -l /usr/lib/x86_64-linux-gnu | grep libopencv

# Clone and build Darknet with OpenCV support
RUN git clone https://github.com/AlexeyAB/darknet.git /darknet && \
    cd /darknet && \
    sed -i 's/OPENCV=0/OPENCV=1/; s/AVX=0/AVX=1/; s/OPENMP=0/OPENMP=1/; s/GPU=1/GPU=0/; s/CUDNN=1/CUDNN=0/' Makefile && \
    make

# Copy Darknet executable to /app
# RUN cp /darknet/darknet /app/darknet

# Set Darknet and OpenCV library paths
ENV DARKNET_PATH=/darknet
ENV LD_LIBRARY_PATH=$DARKNET_PATH:$LD_LIBRARY_PATH:/usr/lib/x86_64-linux-gnu

# Return to the working directory
WORKDIR /app

# Copy the requirements file
COPY requirements.txt .

# Install any needed packages specified in requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the application code
COPY . .

# Make port 8000 available to the world outside this container
EXPOSE 8000

# Define environment variable
ENV PYTHONUNBUFFERED=1

# Run the application
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
