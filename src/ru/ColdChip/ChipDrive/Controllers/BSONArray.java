package ru.ColdChip.ChipDrive.Controllers;

import java.io.*;

public class BSONArray {
	private ByteArrayOutputStream out;
	public BSONArray() {
		this.out = new ByteArrayOutputStream();
	}
	public void put(String right) throws IOException {
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		s.write(1);
		s.write(pack(right.length()));
		s.write(right.getBytes());
		out.write(s.toByteArray());
		s.close();
	}
	public void put(boolean right) throws IOException {
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		s.write(2);
		if(right == true) {
			s.write(1);
		} else {
			s.write(0);
		}
		out.write(s.toByteArray());
		s.close();
	}
	public void put(long right) throws IOException {
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		s.write(3);
		s.write(packLong(right));
		out.write(s.toByteArray());
		s.close();
	}

	public byte[] checkout() {
		return out.toByteArray();
	}

	public static byte[] pack(int i) {
		byte[] result = new byte[4];

		result[0] = (byte) (i);
		result[1] = (byte) (i >> 8);
		result[2] = (byte) (i >> 16);
		result[3] = (byte) (i >> 24);

		return result;
	}

	public static int unpack(byte[] data) {
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

	public static long unpackLong(byte[] data) {
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