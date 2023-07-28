package ru.pocketbyte.locolaser.provider

import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.localizedStringWithFormat
import platform.Foundation.stringWithFormat

class IosStringProvider(
    private val bundle: NSBundle,
    private val tableName: String
) : IndexFormattedStringProvider {

    companion object {
        const val DEFAULT_TABLE_NAME = "Localizable"
    }

    constructor(bundle: NSBundle) : this(bundle, DEFAULT_TABLE_NAME)

    constructor(tableName: String) : this(NSBundle.mainBundle(), tableName)

    constructor() : this(NSBundle.mainBundle(), DEFAULT_TABLE_NAME)

    override fun getString(key: String): String {
        return bundle.localizedStringForKey(key, "", this.tableName)
    }

    override fun getString(key: String, vararg args: Any): String {
        val string = bundle.localizedStringForKey(key, "", this.tableName)
        return when(args.size) {
            0 -> string
            1 -> NSString.stringWithFormat(
                string,
                args[0]
            )
            2 -> NSString.stringWithFormat(
                string,
                args[0], args[1]
            )
            3 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2]
            )
            4 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3]
            )
            5 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4]
            )
            6 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5]
            )
            7 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6]
            )
            8 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]
            )
            9 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]
            )
            10 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9]
            )
            11 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10]
            )
            12 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11]
            )
            13 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12]
            )
            14 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13]
            )
            15 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14]
            )
            16 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15]
            )
            17 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16]
            )
            18 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17]
            )
            19 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18]
            )
            20 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19]
            )
            21 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20]
            )
            22 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21]
            )
            23 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22]
            )
            24 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22], args[23]
            )
            25 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22], args[23], args[24]
            )
            26 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22], args[23], args[24],
                args[25]
            )
            27 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22], args[23], args[24],
                args[25], args[26]
            )
            28 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22], args[23], args[24],
                args[25], args[26], args[27]
            )
            29 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22], args[23], args[24],
                args[25], args[26], args[27], args[28]
            )
            30 -> NSString.stringWithFormat(
                string,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22], args[23], args[24],
                args[25], args[26], args[27], args[28], args[29]
            )
            else -> throw IllegalArgumentException(
                "To many `vararg` arguments. Maximum supported arguments: 30."
            )
        }
    }

    override fun getPluralString(key: String, count: Long, vararg args: Any): String {
        val string = bundle.localizedStringForKey(key, "", this.tableName)
        return when(args.size) {
            0 -> NSString.localizedStringWithFormat(
                string, count
            )
            1 -> NSString.localizedStringWithFormat(
                string, count,
                args[0]
            )
            2 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1]
            )
            3 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2]
            )
            4 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3]
            )
            5 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4]
            )
            6 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5]
            )
            7 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6]
            )
            8 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]
            )
            9 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]
            )
            10 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9]
            )
            11 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10]
            )
            12 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11]
            )
            13 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12]
            )
            14 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13]
            )
            15 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14]
            )
            16 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15]
            )
            17 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16]
            )
            18 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17]
            )
            19 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18]
            )
            20 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19]
            )
            21 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20]
            )
            22 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21]
            )
            23 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22]
            )
            24 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22], args[23]
            )
            25 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22], args[23], args[24]
            )
            26 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22], args[23], args[24],
                args[25]
            )
            27 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22], args[23], args[24],
                args[25], args[26]
            )
            28 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22], args[23], args[24],
                args[25], args[26], args[27]
            )
            29 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22], args[23], args[24],
                args[25], args[26], args[27], args[28]
            )
            30 -> NSString.localizedStringWithFormat(
                string, count,
                args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8],
                args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16],
                args[17], args[18], args[19], args[20], args[21], args[22], args[23], args[24],
                args[25], args[26], args[27], args[28], args[29]
            )
            else -> throw IllegalArgumentException(
                "To many `vararg` arguments. Maximum supported arguments: 30."
            )
        }
    }
}
