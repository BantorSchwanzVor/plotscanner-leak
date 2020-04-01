package org.newdawn.slick.openal;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.OpenALException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

public class OpenALStreamPlayer {
  public static final int BUFFER_COUNT = 3;
  
  private static final int sectionSize = 81920;
  
  private byte[] buffer = new byte[81920];
  
  private IntBuffer bufferNames;
  
  private ByteBuffer bufferData = BufferUtils.createByteBuffer(81920);
  
  private IntBuffer unqueued = BufferUtils.createIntBuffer(1);
  
  private int source;
  
  private int remainingBufferCount;
  
  private boolean loop;
  
  private boolean done = true;
  
  private AudioInputStream audio;
  
  private String ref;
  
  private URL url;
  
  private float pitch;
  
  private float positionOffset;
  
  public OpenALStreamPlayer(int source, String ref) {
    this.source = source;
    this.ref = ref;
    this.bufferNames = BufferUtils.createIntBuffer(3);
    AL10.alGenBuffers(this.bufferNames);
  }
  
  public OpenALStreamPlayer(int source, URL url) {
    this.source = source;
    this.url = url;
    this.bufferNames = BufferUtils.createIntBuffer(3);
    AL10.alGenBuffers(this.bufferNames);
  }
  
  private void initStreams() throws IOException {
    OggInputStream audio;
    if (this.audio != null)
      this.audio.close(); 
    if (this.url != null) {
      audio = new OggInputStream(this.url.openStream());
    } else {
      audio = new OggInputStream(ResourceLoader.getResourceAsStream(this.ref));
    } 
    this.audio = audio;
    this.positionOffset = 0.0F;
  }
  
  public String getSource() {
    return (this.url == null) ? this.ref : this.url.toString();
  }
  
  private void removeBuffers() {
    IntBuffer buffer = BufferUtils.createIntBuffer(1);
    int queued = AL10.alGetSourcei(this.source, 4117);
    while (queued > 0) {
      AL10.alSourceUnqueueBuffers(this.source, buffer);
      queued--;
    } 
  }
  
  public void play(boolean loop) throws IOException {
    this.loop = loop;
    initStreams();
    this.done = false;
    AL10.alSourceStop(this.source);
    removeBuffers();
    startPlayback();
  }
  
  public void setup(float pitch) {
    this.pitch = pitch;
  }
  
  public boolean done() {
    return this.done;
  }
  
  public void update() {
    float sampleSize;
    if (this.done)
      return; 
    float sampleRate = this.audio.getRate();
    if (this.audio.getChannels() > 1) {
      sampleSize = 4.0F;
    } else {
      sampleSize = 2.0F;
    } 
    int processed = AL10.alGetSourcei(this.source, 4118);
    while (processed > 0) {
      this.unqueued.clear();
      AL10.alSourceUnqueueBuffers(this.source, this.unqueued);
      int bufferIndex = this.unqueued.get(0);
      float bufferLength = AL10.alGetBufferi(bufferIndex, 8196) / sampleSize / sampleRate;
      this.positionOffset += bufferLength;
      if (stream(bufferIndex)) {
        AL10.alSourceQueueBuffers(this.source, this.unqueued);
      } else {
        this.remainingBufferCount--;
        if (this.remainingBufferCount == 0)
          this.done = true; 
      } 
      processed--;
    } 
    int state = AL10.alGetSourcei(this.source, 4112);
    if (state != 4114)
      AL10.alSourcePlay(this.source); 
  }
  
  public boolean stream(int bufferId) {
    try {
      int count = this.audio.read(this.buffer);
      if (count != -1) {
        this.bufferData.clear();
        this.bufferData.put(this.buffer, 0, count);
        this.bufferData.flip();
        int format = (this.audio.getChannels() > 1) ? 4355 : 4353;
        try {
          AL10.alBufferData(bufferId, format, this.bufferData, this.audio.getRate());
        } catch (OpenALException e) {
          Log.error("Failed to loop buffer: " + bufferId + " " + format + " " + count + " " + this.audio.getRate(), (Throwable)e);
          return false;
        } 
      } else if (this.loop) {
        initStreams();
        stream(bufferId);
      } else {
        this.done = true;
        return false;
      } 
      return true;
    } catch (IOException e) {
      Log.error(e);
      return false;
    } 
  }
  
  public boolean setPosition(float position) {
    try {
      float sampleSize;
      if (getPosition() > position)
        initStreams(); 
      float sampleRate = this.audio.getRate();
      if (this.audio.getChannels() > 1) {
        sampleSize = 4.0F;
      } else {
        sampleSize = 2.0F;
      } 
      while (this.positionOffset < position) {
        int count = this.audio.read(this.buffer);
        if (count != -1) {
          float bufferLength = count / sampleSize / sampleRate;
          this.positionOffset += bufferLength;
          continue;
        } 
        if (this.loop) {
          initStreams();
        } else {
          this.done = true;
        } 
        return false;
      } 
      startPlayback();
      return true;
    } catch (IOException e) {
      Log.error(e);
      return false;
    } 
  }
  
  private void startPlayback() {
    AL10.alSourcei(this.source, 4103, 0);
    AL10.alSourcef(this.source, 4099, this.pitch);
    this.remainingBufferCount = 3;
    for (int i = 0; i < 3; i++)
      stream(this.bufferNames.get(i)); 
    AL10.alSourceQueueBuffers(this.source, this.bufferNames);
    AL10.alSourcePlay(this.source);
  }
  
  public float getPosition() {
    return this.positionOffset + AL10.alGetSourcef(this.source, 4132);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\openal\OpenALStreamPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */