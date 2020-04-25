package ru.ColdChip.ChipDrive.Constants;

import java.util.*;

public class MimeTypes {
	private static HashMap<String, String> mime = new HashMap<String, String>();
	private static boolean isInit = false;
        public static void init() {
		reversePut("application/andrew-inset", "ez");
                reversePut("application/dsptype", "tsp");
                reversePut("application/futuresplash", "spl");
                reversePut("application/hta", "hta");
                reversePut("application/mac-binhex40", "hqx");
                reversePut("application/mac-compactpro", "cpt");
                reversePut("application/mathematica", "nb");
                reversePut("application/msaccess", "mdb");
                reversePut("application/oda", "oda");
                reversePut("application/ogg", "ogg");
                reversePut("application/pdf", "pdf");
                reversePut("application/pgp-keys", "key");
                reversePut("application/pgp-signature", "pgp");
                reversePut("application/pics-rules", "prf");
                reversePut("application/rar", "rar");
                reversePut("application/rdf+xml", "rdf");
                reversePut("application/rss+xml", "rss");
                reversePut("application/zip", "zip");
                reversePut("application/vnd.android.package-archive", 
                        "apk");
                reversePut("application/vnd.cinderella", "cdy");
                reversePut("application/vnd.ms-pki.stl", "stl");
                reversePut(
                        "application/vnd.oasis.opendocument.database", "odb");
                reversePut(
                        "application/vnd.oasis.opendocument.formula", "odf");
                reversePut(
                        "application/vnd.oasis.opendocument.graphics", "odg");
                reversePut(
                        "application/vnd.oasis.opendocument.graphics-template",
                        "otg");
                reversePut(
                        "application/vnd.oasis.opendocument.image", "odi");
                reversePut(
                        "application/vnd.oasis.opendocument.spreadsheet", "ods");
                reversePut(
                        "application/vnd.oasis.opendocument.spreadsheet-template",
                        "ots");
                reversePut(
                        "application/vnd.oasis.opendocument.text", "odt");
                reversePut(
                        "application/vnd.oasis.opendocument.text-master", "odm");
                reversePut(
                        "application/vnd.oasis.opendocument.text-template", "ott");
                reversePut(
                        "application/vnd.oasis.opendocument.text-web", "oth");
                reversePut("application/msword", "doc");
                reversePut("application/msword", "dot");
                reversePut(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        "docx");
                reversePut(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.template",
                        "dotx");
                reversePut("application/vnd.ms-excel", "xls");
                reversePut("application/vnd.ms-excel", "xlt");
                reversePut(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                        "xlsx");
                reversePut(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.template",
                        "xltx");
                reversePut("application/vnd.ms-powerpoint", "ppt");
                reversePut("application/vnd.ms-powerpoint", "pot");
                reversePut("application/vnd.ms-powerpoint", "pps");
                reversePut(
                        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                        "pptx");
                reversePut(
                        "application/vnd.openxmlformats-officedocument.presentationml.template",
                        "potx");
                reversePut(
                        "application/vnd.openxmlformats-officedocument.presentationml.slideshow",
                        "ppsx");
                reversePut("application/vnd.rim.cod", "cod");
                reversePut("application/vnd.smaf", "mmf");
                reversePut("application/vnd.stardivision.calc", "sdc");
                reversePut("application/vnd.stardivision.draw", "sda");
                reversePut(
                        "application/vnd.stardivision.impress", "sdd");
                reversePut(
                        "application/vnd.stardivision.impress", "sdp");
                reversePut("application/vnd.stardivision.math", "smf");
                reversePut("application/vnd.stardivision.writer",
                        "sdw");
                reversePut("application/vnd.stardivision.writer",
                        "vor");
                reversePut(
                        "application/vnd.stardivision.writer-global", "sgl");
                reversePut("application/vnd.sun.xml.calc", "sxc");
                reversePut(
                        "application/vnd.sun.xml.calc.template", "stc");
                reversePut("application/vnd.sun.xml.draw", "sxd");
                reversePut(
                        "application/vnd.sun.xml.draw.template", "std");
                reversePut("application/vnd.sun.xml.impress", "sxi");
                reversePut(
                        "application/vnd.sun.xml.impress.template", "sti");
                reversePut("application/vnd.sun.xml.math", "sxm");
                reversePut("application/vnd.sun.xml.writer", "sxw");
                reversePut(
                        "application/vnd.sun.xml.writer.global", "sxg");
                reversePut(
                        "application/vnd.sun.xml.writer.template", "stw");
                reversePut("application/vnd.visio", "vsd");
                reversePut("application/x-abiword", "abw");
                reversePut("application/x-apple-diskimage", "dmg");
                reversePut("application/x-bcpio", "bcpio");
                reversePut("application/x-bittorrent", "torrent");
                reversePut("application/x-cdf", "cdf");
                reversePut("application/x-cdlink", "vcd");
                reversePut("application/x-chess-pgn", "pgn");
                reversePut("application/x-cpio", "cpio");
                reversePut("application/x-debian-package", "deb");
                reversePut("application/x-debian-package", "udeb");
                reversePut("application/x-director", "dcr");
                reversePut("application/x-director", "dir");
                reversePut("application/x-director", "dxr");
                reversePut("application/x-dms", "dms");
                reversePut("application/x-doom", "wad");
                reversePut("application/x-dvi", "dvi");
                reversePut("application/x-flac", "flac");
                reversePut("application/x-font", "pfa");
                reversePut("application/x-font", "pfb");
                reversePut("application/x-font", "gsf");
                reversePut("application/x-font", "pcf");
                reversePut("application/x-font", "pcf.Z");
                reversePut("application/x-freemind", "mm");
                reversePut("application/x-futuresplash", "spl");
                reversePut("application/x-gnumeric", "gnumeric");
                reversePut("application/x-go-sgf", "sgf");
                reversePut("application/x-graphing-calculator", "gcf");
                reversePut("application/x-gtar", "gtar");
                reversePut("application/x-gtar", "tgz");
                reversePut("application/x-gtar", "taz");
                reversePut("application/x-hdf", "hdf");
                reversePut("application/x-ica", "ica");
                reversePut("application/x-internet-signup", "ins");
                reversePut("application/x-internet-signup", "isp");
                reversePut("application/x-iphone", "iii");
                reversePut("application/x-iso9660-image", "iso");
                reversePut("application/x-jmol", "jmz");
                reversePut("application/x-kchart", "chrt");
                reversePut("application/x-killustrator", "kil");
                reversePut("application/x-koan", "skp");
                reversePut("application/x-koan", "skd");
                reversePut("application/x-koan", "skt");
                reversePut("application/x-koan", "skm");
                reversePut("application/x-kpresenter", "kpr");
                reversePut("application/x-kpresenter", "kpt");
                reversePut("application/x-kspread", "ksp");
                reversePut("application/x-kword", "kwd");
                reversePut("application/x-kword", "kwt");
                reversePut("application/x-latex", "latex");
                reversePut("application/x-lha", "lha");
                reversePut("application/x-lzh", "lzh");
                reversePut("application/x-lzx", "lzx");
                reversePut("application/x-maker", "frm");
                reversePut("application/x-maker", "maker");
                reversePut("application/x-maker", "frame");
                reversePut("application/x-maker", "fb");
                reversePut("application/x-maker", "book");
                reversePut("application/x-maker", "fbdoc");
                reversePut("application/x-mif", "mif");
                reversePut("application/x-ms-wmd", "wmd");
                reversePut("application/x-ms-wmz", "wmz");
                reversePut("application/x-msi", "msi");
                reversePut("application/x-ns-proxy-autoconfig", "pac");
                reversePut("application/x-nwc", "nwc");
                reversePut("application/x-object", "o");
                reversePut("application/x-oz-application", "oza");
                reversePut("application/x-pkcs12", "p12");
                reversePut("application/x-pkcs7-certreqresp", "p7r");
                reversePut("application/x-pkcs7-crl", "crl");
                reversePut("application/x-quicktimeplayer", "qtl");
                reversePut("application/x-shar", "shar");
                reversePut("application/x-shockwave-flash", "swf");
                reversePut("application/x-stuffit", "sit");
                reversePut("application/x-sv4cpio", "sv4cpio");
                reversePut("application/x-sv4crc", "sv4crc");
                reversePut("application/x-tar", "tar");
                reversePut("application/x-texinfo", "texinfo");
                reversePut("application/x-texinfo", "texi");
                reversePut("application/x-troff", "t");
                reversePut("application/x-troff", "roff");
                reversePut("application/x-troff-man", "man");
                reversePut("application/x-ustar", "ustar");
                reversePut("application/x-wais-source", "src");
                reversePut("application/x-wingz", "wz");
                reversePut("application/x-webarchive", "webarchive");
                reversePut("application/x-x509-ca-cert", "crt");
                reversePut("application/x-x509-user-cert", "crt");
                reversePut("application/x-xcf", "xcf");
                reversePut("application/x-xfig", "fig");
                reversePut("application/xhtml+xml", "xhtml");
                reversePut("text/javascript", "js");
                reversePut("audio/3gpp", "3gpp");
                reversePut("audio/amr", "amr");
                reversePut("audio/basic", "snd");
                reversePut("audio/midi", "mid");
                reversePut("audio/midi", "midi");
                reversePut("audio/midi", "kar");
                reversePut("audio/midi", "xmf");
                reversePut("audio/mobile-xmf", "mxmf");
                reversePut("audio/mpeg", "mpga");
                reversePut("audio/mpeg", "mpega");
                reversePut("audio/mpeg", "mp2");
                reversePut("audio/mpeg", "mp3");
                reversePut("audio/mpeg", "m4a");
                reversePut("audio/mpegurl", "m3u");
                reversePut("audio/prs.sid", "sid");
                reversePut("audio/x-aiff", "aif");
                reversePut("audio/x-aiff", "aiff");
                reversePut("audio/x-aiff", "aifc");
                reversePut("audio/x-gsm", "gsm");
                reversePut("audio/x-mpegurl", "m3u");
                reversePut("audio/x-ms-wma", "wma");
                reversePut("audio/x-ms-wax", "wax");
                reversePut("audio/x-pn-realaudio", "ra");
                reversePut("audio/x-pn-realaudio", "rm");
                reversePut("audio/x-pn-realaudio", "ram");
                reversePut("audio/x-realaudio", "ra");
                reversePut("audio/x-scpls", "pls");
                reversePut("audio/x-sd2", "sd2");
                reversePut("audio/x-wav", "wav");
                reversePut("image/bmp", "bmp");
                reversePut("image/gif", "gif");
                reversePut("image/ico", "cur");
                reversePut("image/ico", "ico");
                reversePut("image/ief", "ief");
                reversePut("image/jpeg", "jpeg");
                reversePut("image/jpeg", "jpg");
                reversePut("image/jpeg", "jpe");
                reversePut("image/pcx", "pcx");
                reversePut("image/png", "png");
                reversePut("image/svg+xml", "svg");
                reversePut("image/svg+xml", "svgz");
                reversePut("image/tiff", "tiff");
                reversePut("image/tiff", "tif");
                reversePut("image/vnd.djvu", "djvu");
                reversePut("image/vnd.djvu", "djv");
                reversePut("image/vnd.wap.wbmp", "wbmp");
                reversePut("image/x-cmu-raster", "ras");
                reversePut("image/x-coreldraw", "cdr");
                reversePut("image/x-coreldrawpattern", "pat");
                reversePut("image/x-coreldrawtemplate", "cdt");
                reversePut("image/x-corelphotopaint", "cpt");
                reversePut("image/x-icon", "ico");
                reversePut("image/x-jg", "art");
                reversePut("image/x-jng", "jng");
                reversePut("image/x-ms-bmp", "bmp");
                reversePut("image/x-photoshop", "psd");
                reversePut("image/x-portable-anymap", "pnm");
                reversePut("image/x-portable-bitmap", "pbm");
                reversePut("image/x-portable-graymap", "pgm");
                reversePut("image/x-portable-pixmap", "ppm");
                reversePut("image/x-rgb", "rgb");
                reversePut("image/x-xbitmap", "xbm");
                reversePut("image/x-xpixmap", "xpm");
                reversePut("image/x-xwindowdump", "xwd");
                reversePut("model/iges", "igs");
                reversePut("model/iges", "iges");
                reversePut("model/mesh", "msh");
                reversePut("model/mesh", "mesh");
                reversePut("model/mesh", "silo");
                reversePut("text/calendar", "ics");
                reversePut("text/calendar", "icz");
                reversePut("text/comma-separated-values", "csv");
                reversePut("text/css", "css");
                reversePut("text/html", "htm");
                reversePut("text/html", "html");
                reversePut("text/h323", "323");
                reversePut("text/iuls", "uls");
                reversePut("text/mathml", "mml");
                // add it first so it will be the default for ExtensionFromMimeType
                reversePut("text/plain", "txt");
                reversePut("text/plain", "asc");
                reversePut("text/plain", "text");
                reversePut("text/plain", "diff");
                reversePut("text/plain", "po");     // reserve "pot" for vnd.ms-powerpoint
                reversePut("text/richtext", "rtx");
                reversePut("text/rtf", "rtf");
                reversePut("text/texmacs", "ts");
                reversePut("text/text", "phps");
                reversePut("text/tab-separated-values", "tsv");
                reversePut("text/xml", "xml");
                reversePut("text/x-bibtex", "bib");
                reversePut("text/x-boo", "boo");
                reversePut("text/x-c++hdr", "h++");
                reversePut("text/x-c++hdr", "hpp");
                reversePut("text/x-c++hdr", "hxx");
                reversePut("text/x-c++hdr", "hh");
                reversePut("text/x-c++src", "c++");
                reversePut("text/x-c++src", "cpp");
                reversePut("text/x-c++src", "cxx");
                reversePut("text/x-chdr", "h");
                reversePut("text/x-component", "htc");
                reversePut("text/x-csh", "csh");
                reversePut("text/x-csrc", "c");
                reversePut("text/x-dsrc", "d");
                reversePut("text/x-haskell", "hs");
                reversePut("text/x-java", "java");
                reversePut("text/x-literate-haskell", "lhs");
                reversePut("text/x-moc", "moc");
                reversePut("text/x-pascal", "p");
                reversePut("text/x-pascal", "pas");
                reversePut("text/x-pcs-gcd", "gcd");
                reversePut("text/x-setext", "etx");
                reversePut("text/x-tcl", "tcl");
                reversePut("text/x-tex", "tex");
                reversePut("text/x-tex", "ltx");
                reversePut("text/x-tex", "sty");
                reversePut("text/x-tex", "cls");
                reversePut("text/x-vcalendar", "vcs");
                reversePut("text/x-vcard", "vcf");
                reversePut("video/3gpp", "3gpp");
                reversePut("video/3gpp", "3gp");
                reversePut("video/3gpp", "3g2");
                reversePut("video/dl", "dl");
                reversePut("video/dv", "dif");
                reversePut("video/dv", "dv");
                reversePut("video/fli", "fli");
                reversePut("video/m4v", "m4v");
                reversePut("video/mpeg", "mpeg");
                reversePut("video/mpeg", "mpg");
                reversePut("video/mpeg", "mpe");
                reversePut("video/mp4", "mp4");
                reversePut("video/mpeg", "VOB");
                reversePut("video/quicktime", "qt");
                reversePut("video/quicktime", "mov");
                reversePut("video/vnd.mpegurl", "mxu");
                reversePut("video/x-la-asf", "lsf");
                reversePut("video/x-la-asf", "lsx");
                reversePut("video/x-mng", "mng");
                reversePut("video/x-ms-asf", "asf");
                reversePut("video/x-ms-asf", "asx");
                reversePut("video/x-ms-wm", "wm");
                reversePut("video/x-ms-wmv", "wmv");
                reversePut("video/x-ms-wmx", "wmx");
                reversePut("video/x-ms-wvx", "wvx");
                reversePut("video/x-msvideo", "avi");
                reversePut("video/x-sgi-movie", "movie");
                reversePut("x-conference/x-cooltalk", "ice");
                reversePut("x-epoc/x-sisx-app", "sisx");
	}

	public static void reversePut(String a, String b) {
	       mime.put(b, a);
	}

	public static String get(String ext) {
                if(isInit == false) {
                        init();
                        isInit = true;
                }
		if(ext != null) {
			return mime.get(ext.toLowerCase());
		} else {
			return "application/octet-stream";
		}
	}
}