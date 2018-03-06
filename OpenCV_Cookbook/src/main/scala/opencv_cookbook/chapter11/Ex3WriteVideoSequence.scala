/*
 * Copyright (c) 2011-2014 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jpsacha at gmail.com
 */

package opencv_cookbook.chapter11

import java.io.File

import org.bytedeco.javacpp.opencv_core.Mat
import org.bytedeco.javacpp.opencv_imgproc._
import org.bytedeco.javacpp.opencv_videoio._


/** The example for section "Writing video sequences" in Chapter 10, page 261.
  *
  * Frames are read from a video file, processed, and saved to a new video file using `VideoProcessor` class.
  * Each frame is processed to detect edges using `canny` that is passed as
  * frame processing method to the `VideoProcessor`.
  */
object Ex3WriteVideoSequence extends App {

  // Define a method for processing video frames
  def canny(src: Mat, dest: Mat): Unit = {
    // Convert to gray
    cvtColor(src, dest, COLOR_BGR2GRAY)
    // Compute Canny edges
    Canny(dest, dest, 100, 200, 3, true)
    // Invert the image
    threshold(dest, dest, 128, 255, THRESH_BINARY_INV)
    // Indicate processing progress
    print(".")
  }


  val inputFile = new File("data/bike.avi")
  println("Processing video file: " + inputFile.getCanonicalPath)

  // Create video processor instance
  val processor = new VideoProcessor()
  processor.input = inputFile.getCanonicalPath
  // Do not display video while processing
  processor.displayInput = ""
  processor.displayOutput = ""
  // Play the video at the original frame rate
  processor.delay = 0
  // Set the frame processor callback function
  processor.frameProcessor = canny

  // Decide which codec to use for output video
  val codec = if (System.getProperty("os.name").toLowerCase.startsWith("windows")) {
    CV_FOURCC_PROMPT // prompt used with list of available codecs
  } else {
    0 // Use the same is input
  }
  // Indicate file name and coded to use to write video
  val outputFile = new File("bikeOut.avi")
  println("Processing video file: " + outputFile.getCanonicalPath)
  processor.setOutput(outputFile.getCanonicalPath, codec = codec)

  // Start the process
  processor.run()

  // Close the video file
  println("\nVideo processing done.")
}