package ru.ColdChip.ChipDrive.Controllers;

import ru.ColdChip.WebServer.*;
import org.JSON.JSONException;
import ru.ColdChip.ChipDrive.Exceptions.*;
import java.io.IOException;

public interface IChipDrive {
	public void version(Request request, Response response) throws IOException, ChipDriveException, JSONException;
	public void config(Request request, Response response) throws IOException, ChipDriveException, JSONException;
	public void list(Request request, Response response) throws IOException, ChipDriveException, JSONException;
	public void link(Request request, Response response) throws IOException, ChipDriveException, JSONException;
	public void upload(Request request, Response response) throws IOException, ChipDriveException, JSONException;
	public void delete(Request request, Response response) throws IOException, ChipDriveException, JSONException;
	public void folder(Request request, Response response) throws IOException, ChipDriveException, JSONException;
	public void rename(Request request, Response response) throws IOException, ChipDriveException, JSONException;
	public void info(Request request, Response response) throws IOException, ChipDriveException, JSONException;
	public void quota(Request request, Response response) throws IOException, ChipDriveException, JSONException;
	public void stream(Request request, Response response) throws IOException, ChipDriveException, JSONException;
}