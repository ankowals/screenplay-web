package com.github.ankowals.framework.web.devtools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.TestInfo;

// To install ffmpeg static build on amazon linux
/*
 if [ "$PAGE_SCREENCASTING_ENABLED" = true ] ; then
   yum -y install xz
   yum -y install wget
   mkdir -p /usr/local/bin/ffmpeg
   wget https://johnvansickle.com/ffmpeg/builds/ffmpeg-git-amd64-static.tar.xz -P /usr/local/bin/ffmpeg
   tar -xvf /usr/local/bin/ffmpeg/ffmpeg-git-amd64-static.tar.xz -C /usr/local/bin/ffmpeg
   mv /usr/local/bin/ffmpeg/ffmpeg-git-*-static/* /usr/local/bin/ffmpeg
   ln -s /usr/local/bin/ffmpeg/ffmpeg /usr/bin/ffmpeg
 fi
*/
public class VideoMerger {

  private VideoMerger() {}

  public static void merge(Path screencastDir, File reportDir, TestInfo testInfo)
      throws IOException {
    FFmpegBuilder ffmpegBuilder =
        new FFmpegBuilder()
            .addExtraArgs("-framerate", String.valueOf(10))
            .addInput(screencastDir + "/%d.png")
            .setVideoFilter("pad=iw+mod(iw\\,2):ih+mod(ih\\,2)")
            .addOutput(
                "%s/%s.%s.mp4"
                    .formatted(
                        reportDir,
                        testInfo.getTestClass().orElseThrow().getName(),
                        testInfo.getTestMethod().orElseThrow().getName()))
            .setVideoFrameRate(10, 1)
            .setVideoCodec("h264")
            .setVideoPixelFormat("yuv420p")
            .disableAudio()
            .setConstantRateFactor(10)
            .done();

    FFmpeg ffmpeg = new FFmpeg();
    FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);

    FFmpegJob ffmpegJob = executor.createJob(ffmpegBuilder);

    ffmpegJob.run();

    Awaitility.await()
        .until(
            () ->
                ffmpegJob.getState().equals(FFmpegJob.State.FINISHED)
                    || ffmpegJob.getState().equals(FFmpegJob.State.FAILED));
  }
}
