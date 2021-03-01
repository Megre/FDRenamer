# FDRenamer
 
FDRenamer is a file sorter that filters and sorts files. 

# Usage
    -input <path> 
           [;<path>]... 
    -mode [preview | exec] 
    -processSubdir [true | false] 
    -filter <parameter>=<matcher> 
            [;<parameter>=<matcher>]... 
    -process [action=[move | copy]] 
             [;remainDirStructure=[true | false]] 
             [;onInvalidParam=[ignore | transfer]] 
             [;replaceExisting=[true | false]] 
             [;outputDir=<output_directory>] 
             [;filePathName=<file_path_and_name>] 
             [;renamer=<java_file_path>] 
    -config <configuration_file_path>
    
### Example
    
    java -jar FDRenamer.jar -input D:\files -config yearMonthSorter.cfg
    
The configuration file [yearMonthSorter.cfg](http://https://github.com/Megre/FDRenamer/blob/master/testData/yearMonthSorter.cfg) is as follows:

    -mode exec 
    -processSubdir false  
    
    -filter $fileName=/.+(jpg|jpeg|mp4|gif)/
    -process "action=move;
        outputDir=.;
        onInvalidParam=transfer;
        filePathName=${mediaCreationDate, yyyy-MM}/${fileName};" 
    
    # WeChat export file
    -filter $fileName=/.*(\d{13}.*jpg|\d{12}.*mp4)/
    -process "action=move;
        outputDir=.;
        onInvalidParam=transfer;
        filePathName=$renamer/$fileName;
        renamer=testData/TimeStampRenamer.java" 
    
    # yyyy-MM-dd
    -filter $fileName=/.*(\d{4}).?(\d{2}).?\d{2}.+/
    -process "action=move;
        outputDir=.;
        filePathName=${: cg($0.1-$0.2)}/$fileName"
  
# Options

### -input 

`<path>` indicates the input directory or file.

### -mode

In `preview` mode, FDRenamer simulates the processing and displays warnings and errors, while in `exec` mode FDRenamer executes the processing.

### -processSubdir

Specify `true` to process the sub-directory of the input directory.

### -filter

FDRenamer follows a Pipes and Filters architecture. Input files are processed sequentially: 

    input files -> filter0 -> processor0 -> filter1 -> processor1 -> ...

The files matched with a filter are processed by the processor followed. Non-matched files are transferred to subsequent filters. 

Thus, `-filter` and `-process` options occur in pair leading by the `-filter` option.

A filter is a `<parameter>`-`<matcher>` (see [parameter](#parameter) and [matcher](#matcher)) pair in which the value of the parameter is matched with the matcher. If multiple `<parameter>`-`<matcher>` pairs are specified (separated by ";"), all pairs should be matched.

### -process

This option specifies how to process the files matched with the filter ahead.

`action` - indicates the action to perform: move or copy files to the output directory (`outputDir`).

`remainDirStructure` - `true` to remain the directory structure of the input file (relative to the input directory where the input file resides in) when the file is copied or moved to the output directory.

`onInvalidParam` - indicates the action when failing to process a matched file (e.g. a parameter is invalid): `ignore` to consume the file (doesn't transfer to the next filter) or `transfer` to transfer to the next filter.

`replaceExisting` - `true` to replace an existing file when an input file is copied or moved to the output directory.

`outputDir` - specifies the output directory (use "." to specify the directory same to the input file). This option can use a [parameter](#parameter) and a [renamer](#renamer).

`filePathName` - specifies the path (relative to `outputDir`) and name of output file. This option can use a [parameter](#parameter) and a [renamer](#renamer).

`renamer` - a [renamer](#renamer) provides a programming interface to the user. A renamer is a user-defined Java class that implements group.spart.fdr.attr.FileRenamer (e.g. TimeStampRenamer). It has a "rename" method which returns a string. FDRenamer compiles the Java class and invoke the "rename" method. The returned string will replace the renamer parameter (i.e. "$renamer") contained in an option.

### -config

All the options can be specified either in command lines or in a configuration file. Both ways can also be mixed, e.g. some options are specified in command lines and the others in a configuration file.

The character encoding uses "UTF-8", so does the configuration file.

### Default Option Values

If not specified, the options take the following values: 

    -mode preview
    -processSubdir false
    -process action=copy
    -process replaceExisting=false
    -process remainDirStructure=true
    -process outputDir=./
    -process filePathName=${fileName}
    -process onInvalidParam=ignore

# Parameter and Matcher

### Parameter

A parameter formats and modifies an [attribute](#attribute) of an input file. A parameter is specified as:

    $<name>
    or
    ${<name> [, <formatter>] [: <modifier>] ... [| [, <formatter>] [: <modifier>] ...] ...}
    
`<name>` is the name of the attribute, e.g. `fileName`, `fileSize`, and `creationDate`, etc.

`<formatter>` formats the value of the given attribute, e.g `${fileSize, kb, %.2f}` formats the size of file in KB resulting in a float value and then formats the float value to two decimal places.

`<modifier>` modifies a value, e.g. `${fileName: case(uc)}` turns the file name to uppercase.

A formatter or modifier can follow each other to successively revise the value of an attribute, e.g. `${fileName: case(uc), %10s}` first turns the file name to uppercase then extends the resulting value to the minimal width of 10 by prefixing white spaces.

A parameter may contain multiple entries separated by `|`. In this case, the value of the parameter is the first available entry. For example, `${mediaCreationDate, yyyy-MM | creationDate, yyyy-MM}` returns the media creation date if possible, or it returns the creation date of the input file.

### Attribute

Here is a list of currently supported attributes:

- fileName (including the extension)
- fileNameExt
- fileNameNoExt
- filePath
- fileSize: the size of a file in bytes. The following formatters can be used to change the measuring unit: `bit`, `b` (byte), `kb`, `mb`, `gb`, and `tb`.
- rootDir. The root directory of the input file.
- parentDirPath
- parentDirName
- creationDate
- lastAccessDate
- lastModifiedDate
- currentDate
- mediaCreationDate: the creation date of the media (e.g. jpg, gif or mp4).
- desktop: the path of desktop directory.
- system variables. The variables defined in the system environment, e.g. `path`.

Attribute names are case insensitive.

### Formatter

There are four types of formatter:

- Date formatter. See [java.text.SimpleDateFormat](https://docs.oracle.com/javase/9/docs/api/java/text/SimpleDateFormat.html). For example, `{$creationDate, yyyy-MM}` formats the creation date as "2021-02". This formatter accepts a date (e.g. `creationDate`) and outputs a string.
- File size formatter: one of `bit`, `b` (byte), `kb`, `mb`, `gb`, and `tb`. This formatter accepts the input of `fileSize` and outputs a float.
- String formatter. See [java.lang.String.format](https://docs.oracle.com/javase/9/docs/api/java/lang/String.html#format-java.lang.String-java.lang.Object...-). This formatter accepts a string and outputs a string.
- Float formatter. See [java.lang.String.format](https://docs.oracle.com/javase/9/docs/api/java/lang/String.html#format-java.lang.String-java.lang.Object...-). This formatter accepts a float and outputs a string.

### Modifier

Three types of modifiers are:

- capture group (`cg(<reference_format>)`). A capture group refers to the groups captured by a [regular expression matcher](#regular-expression-matcher) specified in the filter. For example, in the option `-filter $fileName=/.*(\d{4}).?(\d{2}).?\d{2}.+/ -process filePathName=${: cg($0.1-$0.2)}/$fileName...`, `cg($0.1-$0.2)` is a capture group modifier. The `<reference_format>` is a string that contains references to captured groups. A reference of a capture group is in the format of `$<filter_index>.<group_index>` in which `<filter_index>` is the index of the `<parameter>`-`<matcher>` pair in the filter option; `<group_index>` is the index of the captured group. To use a capture group modifier, the `<matcher>` should be a [regular expression matcher](#regular-expression-matcher). The name of a parameter can be empty if the parameter starts with a capture group modifier.

- case (`case(<instruction>)`): modifies the case of input string. The `<instruction>` is one of `uc` (uppercase all letters), `lc` (lowercase all letters), `ucfl` (uppercase the first letter), `lcfl` (lowercase the first letter), `ucw` (uppercase the first letter of each word), `lcw` (lowercase the first letter of each word), or `ac` (alternate the case of each letter). Multiple case modifiers can be combined to achieve specific results. For example, the file name "A little cat.jpg" can be modified to "a lITTLE cAT.JPG" using `${fileName, case(lc), case(ucw), case(ac)}`.

- sub-string (`substr(<begin_index>)` or `substr(<begin_index>, <end_index>)`). This modifier takes a sub-string between `<begin_index>` (inclusive) and `<end_index>` (exclusive).

### Renamer

A renamer is a special parameter, i.e. `$renamer` or `${renamer}`. To use a renamer, the `renamer` option should be specified within the `-process` option to provide a user-defined Java file (see [example](#example)). Here is an example of a user-defined renamer [TimeStampRenamer.java](https://github.com/Megre/FDRenamer/blob/master/src/TimeStampRenamer.java):

    import java.text.SimpleDateFormat;
    import java.util.Date;
    import java.util.regex.Matcher;
    import java.util.regex.Pattern;
    
    import group.spart.fdr.FDRFilter;
    import group.spart.fdr.attr.FileAttribute;
    import group.spart.fdr.attr.FileRenamer;
    
    public class TimeStampRenamer implements FileRenamer {
     
    	/**
    	 * @see group.spart.fdr.attr.FileRenamer#rename(group.spart.fdr.attr.FileAttribute)
    	 */
    	@Override
    	public String rename(FileAttribute fileAttribute, FDRFilter filter) { 
    		if(fileAttribute == null) return null;
    		 
    		String fileName = fileAttribute.inflate("$fileName", filter); 
    		 
    		// WeChat export format
    		Pattern regexTimeStamp = Pattern.compile(".*?(\\d{13}).*");
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    		Matcher matcher = regexTimeStamp.matcher(fileName);
    		if(matcher.matches()) {
    			return dateFormat.format(new Date(Long.parseLong(matcher.group(1))));
    		}
    
    		// WeChat export format
    		Pattern regexLongDate = Pattern.compile(".*?(\\d{12}).*");
    		matcher = regexLongDate.matcher(fileName);
    		if(matcher.matches()) {
    			String result = matcher.group(1);
    			return new SimpleDateFormat("yyyy").format(new Date()).substring(0, 2) + result.substring(10, 12) + "-" 
    				+ result.substring(8, 10);
    		}
    		
    		return fileName;
    	}
    
    }

A renamer should implement [group.spart.fdr.attr.FileRenamer](https://github.com/Megre/FDRenamer/blob/master/src/group/spart/fdr/attr/FileRenamer.java) and rewrite the rename method. The [FileAttribute](https://github.com/Megre/FDRenamer/blob/master/src/group/spart/fdr/attr/FileAttribute.java) represents the file attributes. Its "inflate" method calculates the value of a given parameter.

### Matcher

A matcher is used in a `-filter` option. FDRenamer supports three types of matcher:

- regular expression matcher: `/<regex>/ [<flag>] [, <flag>] ...`. `<regex>` is a regular expression. `<flag>` can be one of `CASE_INSENSITIVE`, `MULTILINE`, `DOTALL`, `UNICODE_CASE`, `CANON_EQ`, `UNIX_LINES`, `LITERAL`, `UNICODE_CHARACTER_CLASS`, and `COMMENTS`. (see [java.util.regex.Pattern](https://docs.oracle.com/javase/9/docs/api/java/util/regex/Pattern.html))

- range matcher: `<left_indicator> <left_range>, <right_range> <right_indicator>`. `<left_indicator>` can be one of `[` (inclusive) or `(` (exclusive); `<right_indicator>` can be one of `]` (inclusive) or `)` (exclusive). `<left_range>` and `<right_range>` indicate a range between them. If `<left_range>` or `<right_range>` is empty, it means infinity and always returns true on that range. For example, `[,2020)` doesn't match "2020"; `(,2020]` matches "2020"; `[2020,)` doesn't match "2019"; `[2020,)` matches "2020".
		
- string matcher. `<string>`. A string matcher exactly matches a string. For example, `-filter $fileName=abc.jpg` exactly matches a file whose name is "abc.jpg" (case sensitive). To be case insensitive, the filter can be specified as `${fileName: case(lc)}=abc.jpg`.


# Special Characters

The option, modifier, or formatter use some special separator characters including:

    ;=${}|()[]%
    
To correctly parse the option, modifier, or formatter, URL encoding should be used to allow special characters (e.g. using "%2c" for "," and "%3a" for ":") (see [java.net.URLEncoder](https://docs.oracle.com/javase/9/docs/api/java/net/URLEncoder.html))

# Requirements

- FDRenamer depends on [JDK 1.8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html) or later.

FDRenamer extracts some file attributes based on [metadata-extractor](https://github.com/drewnoakes/metadata-extractor).
