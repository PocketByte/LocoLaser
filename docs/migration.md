## Migration from 1.5.* to 2.0.0
Starting from version 2.0.0 all Platforms and Sources become Resources, so you can use as a Platform or as a Source any resource that you want.
For example you can use iOS strings as a Source to generate Swift Class.

#### Dependencies
All maven artifacts of resources now has prefix "resource". So following dependencies should be changed:  
`platform-mobile`        -> `resource-mobile`  
`platform-kotlin-mobile` -> `resource-kotlin-mobile`  
`platform-json`          -> `resource-json`  
`platform-gettext`       -> `resource-gettext`  
`source-googlesheet`     -> `resource-googlesheet`  

#### List of Locales 
Google Sheet config now has no field `"column_locales"`. This field moved to the general layer and renamed to `"locales"`.  
**IMPORTANT**: Field `"locales"` are mandatory! If you want to use only base locale you could provide value `["base"]`.

Version 1.5.*
```json
{
    "platform" : "android",
    "source" : {
        "type" : "googlesheet",
        "column_key" : "key",
        "column_locales" : ["en", "fi"],
        "id" : "1KDu0_iel5qoNTKHZI0e4l3Uy52WisdfswYRy_GlFOPtY"
    }
}
```

Version 2.0.0
```json
{
    "platform" : "android",
    "source" : {
        "type" : "googlesheet",
        "column_key" : "key",
        "id" : "1KDu0_iel5qoNTKHZI0e4l3Uy52WisdfswYRy_GlFOPtY"
    },
    "locales" : ["en", "fi"]
}
```