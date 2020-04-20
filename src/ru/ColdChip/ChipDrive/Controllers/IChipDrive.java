package ru.ColdChip.ChipDrive.Controllers;

import ru.ColdChip.WebServer.*;
import java.io.IOException;

public interface IChipDrive {
	public void version(Request request, Response response) throws IOException;
	public void driveConfig(Request request, Response response) throws IOException;
	public void fileList(Request request, Response response) throws IOException;
	public void fileLink(Request request, Response response) throws IOException;
	public void fileUpload(Request request, Response response) throws IOException;
	public void fileDelete(Request request, Response response) throws IOException;
	public void newFolder(Request request, Response response) throws IOException;
	public void fileRename(Request request, Response response) throws IOException;
	public void fileInfo(Request request, Response response) throws IOException;
	public void driveQuota(Request request, Response response) throws IOException;
	public void fileStream(Request request, Response response) throws IOException;
}