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
    python3-dev && \
    apt-get clean
    
# Clone OpenCV and OpenCV contrib repositories
RUN git clone https://github.com/opencv/opencv.git && \
git clone https://github.com/opencv/opencv_contrib.git

# Create a build directory
RUN mkdir -p opencv/build

# Configure OpenCV build
WORKDIR /app/opencv/build
RUN cmake -D CMAKE_BUILD_TYPE=RELEASE \
    -D CMAKE_INSTALL_PREFIX=/usr/local \
    -D OPENCV_EXTRA_MODULES_PATH=../../opencv_contrib/modules \
    -D BUILD_EXAMPLES=OFF ..

# Build and install OpenCV
RUN make -j$(nproc) && \
make install && \
ldconfig

# Clone and build Darknet with OpenCV support
RUN git clone https://github.com/AlexeyAB/darknet.git /darknet && \
    cd /darknet && \
    sed -i 's/OPENCV=0/OPENCV=1/; s/AVX=0/AVX=1/; s/OPENMP=0/OPENMP=1/' Makefile && \
    make

# Set Darknet as a library path
ENV DARKNET_PATH=/darknet
ENV LD_LIBRARY_PATH=$DARKNET_PATH:$LD_LIBRARY_PATH

# Return to the working directory
WORKDIR /app

# Copy the requirements file
COPY requirements.txt .
COPY ./ai_model/yolov4_final.weights ./ai_model/yolov4_final.weights

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
