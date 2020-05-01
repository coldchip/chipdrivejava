package ru.ColdChip.ChipDrive.Controllers;

import ru.ColdChip.WebServer.*;
import java.io.IOException;

public interface IChipDrive {
	public void enqueue(int method, Request request, Response response) throws IOException;
}