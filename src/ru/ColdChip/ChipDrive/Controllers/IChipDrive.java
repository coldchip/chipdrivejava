package ru.ColdChip.ChipDrive.Controllers;

import ru.ColdChip.WebServer.*;
import java.io.IOException;

public interface IChipDrive {
	public void version(Request request, Response response) throws IOException;
	public void config(Request request, Response response) throws IOException;
	public void list(Request request, Response response) throws IOException;
	public void link(Request request, Response response) throws IOException;
	public void upload(Request request, Response response) throws IOException;
	public void delete(Request request, Response response) throws IOException;
	public void folder(Request request, Response response) throws IOException;
	public void rename(Request request, Response response) throws IOException;
	public void info(Request request, Response response) throws IOException;
	public void quota(Request request, Response response) throws IOException;
	public void stream(Request request, Response response) throws IOException;
}