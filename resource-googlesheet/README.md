# Source: Google Sheet

### Gradle dependency
```gradle
dependencies {
    localize 'ru.pocketbyte.locolaser:resource-googlesheet:2.0.0'
}
```
### Config
Google Sheet source config is a JSON object that has following structure:
```
{
    "type" : "googlesheet",
    "id" : (String value),
    "worksheet_title" : (String value),
    "column_key" : (String value),
    "column_quantity" : (String value),
    "column_comment" : (String value),
    "column_metadata" : (String value),
    "credential_file" : (Path to file)
}
```
Each field in JSON has following purpose:
- **`type`** - String. Type of the source. Must be equal `"googlesheet"`.
- **`id`** - String. ID of the Google Sheet. You can get it from sheet url (https://docs.google.com/spreadsheets/d/**sheet_id**).
- **`worksheet_title`** - Title of the worksheet with localized strings. Not necessary property, by default will used first worksheet of the sheet.
- **`column_key`** - String. Column title which contain string key's.
- **`column_quantity`** - String. Column title which contain quantity. Not necessary property, by default no quantities.
- **`column_comment`** - String. Column title which contain comment. Not necessary property, by default no comments.
- **`column_metadata`** - String. Column title which contain Metadata of the row. Not necessary property, by default no metadata. Pattern of the metadata cell value: "[key_name_1]=[value];[key_name_2]=[value];...".
- **`credential_file`** - String. Path to OAUth credential file. Not necessary property.

You can use keyword `base` to specify base locale. If base locale not set or column with base locale not exists, first locale in provided locales list will import as base locale also.

### Example
Here is the example of LocoLaser config where as a source used Google Sheet
```json
{
    "platform" : "android",
    "source" : {
        "type" : "googlesheet",
        "column_key" : "key",
        "id" : "1KDu0_iel5qoNTKHZI0e4l3Uy52WisdfswYRy_GlFOPtY"
    },
    "locales" : ["en", "fi"],
    "delay" : 30
}
```