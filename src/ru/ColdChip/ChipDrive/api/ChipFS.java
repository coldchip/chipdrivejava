package ru.ColdChip.ChipDrive.api;

import java.io.*;
import java.util.*;

public class ChipFS {

	private byte[] key = "2SqxJCHaf5kjfsz8QESU7tj2mfNGad4Dchy3jDwyG3MUR4KHx7BjHWYed9QaWtbv9t6WCT2RfVQXmrAchP8nSLPZtwQKzV4Cx9X732P9NhuETFRY2mrtE7Eyn8HZG7gyyGk7TfT928DpBxRvPMCVLya87kxtGupwp5nVNnUXsX7XAtyFBRhqfDDrAsU5YWFvZ62TPrRKcEGRcLf2JJma8QgKbSH2uyXTGPbvcUz2ARaqrGCaN3UBZkMHDnW9UvKkSP5WajuHwjE4FgdANED28mxgrDTAPR3uTP7LaW9gjhyyvyGj4YZnUMhZVyEeqfWxWykUKRkGqVSzzECG9g3yq8b5K4nY7JQZyHw6XxbgxEnA2ssgqbjUuhLCAPfDWwKF9S8zjtmnebRqLKkTAcGMQwxp3be4aAUfZufyYY4WQc6nLJaNvVm6grDKGRFwVDgZQvXEPRstkcLJzZpvRkEPnS4U3jNcxt87rcZ4RWzDpbDKS7gG6Y23qKQ2kjZ8Cp7XKxAtwZyM64UfSZd4JwSsz8nmEzFvaZHpKyC5ZGs8j4TSAxHGdkgXCFEct6MLzWDBJP8WDeeaqpGzYUtbwEF9AWHqApCCA2h5ftKsH2Emw55x4vJ65zA4a7tneNkM6JR9TJFxa3n8ZtjphTynqtR5cBGjcYKUjqr8PALrptGVDXa7wZM7Dy8mpQWHaDW6pMqLxQJsabuNkwDwyzC88PPUcTtVjPRmZYdMvDfePa33nNDLxAgmxU3VC3B6mX6tAsXAj2EmKCzeH94ZwqKkh92Ls3qtFyeEAyqCtygUEQbec8FDtYMXuWmjYNYfAzRXMuCzTCXJheFjcWnjeCNd6Ky2kCjZxAsYwSw2ZYKjbkjjPajnCzEmHq6u8DwbPr8egRTRywuuT8Sp4qSdaTDuXhWFGDJM2LFAaECFVdTWVNWPg5mJUk6zyxt59e4XzHYkgHj5NcFQZm9WxZdpWgFgFEbT5cTFGpXrVzfz88sQGBXGP4D7XMVyW8bJjLFj3H93xMeX".getBytes();
	private String root = "buckets";
	private LinkedHashMap<String, RandomAccessFile> lookup = new LinkedHashMap<String, RandomAccessFile>();

	public ChipFS() {
		
	}

	public boolean create(String id) {
		try {
			return new File(root + "/" + id).createNewFile();
		} catch(Exception e) {
			return false;
		}
	}

	public boolean delete(String id) {
		try {
			return new File(root + "/" + id).delete();
		} catch(Exception e) {
			return false;
		}
	}

	public boolean exists(String id) {
		
		return new File(this.root + "/" + id).exists();
	}

	public long size(String id) {
		return new File(root + "/" + id).length();
	}

	private RandomAccessFile getHandler(String id) throws IOException {
		if(this.lookup.containsKey(id)) {
			return this.lookup.get(id);
		} else {
			if(this.lookup.size() > 63) {
				Map.Entry<String, RandomAccessFile> entry = this.lookup.entrySet().iterator().next();
				entry.getValue().close();
				this.lookup.remove(entry.getKey());
			}
			RandomAccessFile io = new RandomAccessFile(new File(root + "/" + id), "rw");
			this.lookup.put(id, io);
			return io;
		}
	}

	public boolean read(String id, byte[] buffer, long offset, int size) throws IOException {
		RandomAccessFile io = getHandler(id);
		io.seek(offset);
		io.read(buffer, 0, size);
		//for(int i = 0; i < buffer.length; i++) {
		//	buffer[i] = (byte)(buffer[i] ^ key[i % key.length]);
		//}
		return true;
	}

	public boolean write(String id, byte[] buffer, long offset, int size) throws IOException {
		//for(int i = 0; i < size; i++) {
		//	buffer[i] = (byte)(buffer[i] ^ key[i % key.length]);
		//}
		RandomAccessFile io = getHandler(id);
		io.seek(offset);
		io.write(buffer, 0, size);
		return true;
	}

	public static byte[] packInt(int i) {
		byte[] result = new byte[4];

		result[0] = (byte) (i);
		result[1] = (byte) (i >> 8);
		result[2] = (byte) (i >> 16);
		result[3] = (byte) (i >> 24);

		return result;
	}

	public static int unpackInt(byte[] data) {
		return (data[0] & 255) | 
		((data[1] & 255) << 8) | 
		((data[2] & 255) << 16) | 
		((data[3] & 255) << 24);
	}

	public static byte[] packLong(long i) {
		byte[] result = new byte[8];

		result[0] = (byte) (i);
		result[1] = (byte) (i >> 8);
		result[2] = (byte) (i >> 16);
		result[3] = (byte) (i >> 24);
		result[4] = (byte) (i >> 32);
		result[5] = (byte) (i >> 40);
		result[6] = (byte) (i >> 48);
		result[7] = (byte) (i >> 56);

		return result;
	}

	public static int unpackLong(byte[] data) {
		return (data[0] & 255) | 
		((data[1] & 255) << 8) | 
		((data[2] & 255) << 16) | 
		((data[3] & 255) << 24) |
		((data[4] & 255) << 32) | 
		((data[5] & 255) << 40) | 
		((data[6] & 255) << 48) | 
		((data[7] & 255) << 56);
	} 
}