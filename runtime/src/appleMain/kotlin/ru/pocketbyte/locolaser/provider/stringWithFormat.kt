package ru.pocketbyte.locolaser.provider

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

internal fun String.stringWithFormat(vararg args: Any): String {
    return when(args.size) {
        0 -> this
        1 -> NSString.stringWithFormat(
            this, args[0]
        )
        2 -> NSString.stringWithFormat(
            this, args[0], args[1]
        )
        3 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2]
        )
        4 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3]
        )
        5 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4]
        )
        6 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5]
        )
        7 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6]
        )
        8 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]
        )
        9 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8]
        )
        10 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9]
        )
        11 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10]
        )
        12 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11]
        )
        13 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12]
        )
        14 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13]
        )
        15 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14]
        )
        16 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15]
        )
        17 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16]
        )
        18 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17]
        )
        19 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18]
        )
        20 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19]
        )
        21 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20]
        )
        22 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21]
        )
        23 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22]
        )
        24 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23]
        )
        25 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24]
        )
        26 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25]
        )
        27 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26]
        )
        28 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27]
        )
        29 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28]
        )
        30 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29]
        )
        31 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30]
        )
        32 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31]
        )
        33 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32]
        )
        34 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33]
        )
        35 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34]
        )
        36 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35]
        )
        37 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36]
        )
        38 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37]
        )
        39 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38]
        )
        40 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39]
        )
        41 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40]
        )
        42 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41]
        )
        43 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42]
        )
        44 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43]
        )
        45 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44]
        )
        46 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45]
        )
        47 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46]
        )
        48 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47]
        )
        49 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48]
        )
        50 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49]
        )
        51 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50]
        )
        52 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51]
        )
        53 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52]
        )
        54 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53]
        )
        55 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54]
        )
        56 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55]
        )
        57 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56]
        )
        58 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57]
        )
        59 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58]
        )
        60 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59]
        )
        61 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60]
        )
        62 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61]
        )
        63 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62]
        )
        64 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63]
        )
        65 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64]
        )
        66 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65]
        )
        67 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66]
        )
        68 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67]
        )
        69 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68]
        )
        70 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69]
        )
        71 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70]
        )
        72 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71]
        )
        73 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72]
        )
        74 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73]
        )
        75 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74]
        )
        76 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75]
        )
        77 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76]
        )
        78 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77]
        )
        79 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78]
        )
        80 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79]
        )
        81 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80]
        )
        82 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81]
        )
        83 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82]
        )
        84 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83]
        )
        85 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84]
        )
        86 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85]
        )
        87 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86]
        )
        88 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87]
        )
        89 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88]
        )
        90 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89]
        )
        91 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90]
        )
        92 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91]
        )
        93 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92]
        )
        94 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93]
        )
        95 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94]
        )
        96 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95]
        )
        97 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96]
        )
        98 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97]
        )
        99 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98]
        )
        100 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99]
        )
        101 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100]
        )
        102 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101]
        )
        103 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102]
        )
        104 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103]
        )
        105 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104]
        )
        106 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105]
        )
        107 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106]
        )
        108 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107]
        )
        109 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108]
        )
        110 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109]
        )
        111 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110]
        )
        112 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111]
        )
        113 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112]
        )
        114 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112], args[113]
        )
        115 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112], args[113], args[114]
        )
        116 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112], args[113], args[114], args[115]
        )
        117 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112], args[113], args[114], args[115], args[116]
        )
        118 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112], args[113], args[114], args[115], args[116], args[117]
        )
        119 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112], args[113], args[114], args[115], args[116], args[117], args[118]
        )
        120 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112], args[113], args[114], args[115], args[116], args[117], args[118], args[119]
        )
        121 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112], args[113], args[114], args[115], args[116], args[117], args[118], args[119],
            args[120]
        )
        122 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112], args[113], args[114], args[115], args[116], args[117], args[118], args[119],
            args[120], args[121]
        )
        123 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112], args[113], args[114], args[115], args[116], args[117], args[118], args[119],
            args[120], args[121], args[122]
        )
        124 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112], args[113], args[114], args[115], args[116], args[117], args[118], args[119],
            args[120], args[121], args[122], args[123]
        )
        125 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112], args[113], args[114], args[115], args[116], args[117], args[118], args[119],
            args[120], args[121], args[122], args[123], args[124]
        )
        126 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112], args[113], args[114], args[115], args[116], args[117], args[118], args[119],
            args[120], args[121], args[122], args[123], args[124], args[125]
        )
        127 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112], args[113], args[114], args[115], args[116], args[117], args[118], args[119],
            args[120], args[121], args[122], args[123], args[124], args[125], args[126]
        )
        128 -> NSString.stringWithFormat(
            this, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7],
            args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15],
            args[16], args[17], args[18], args[19], args[20], args[21], args[22], args[23],
            args[24], args[25], args[26], args[27], args[28], args[29], args[30], args[31],
            args[32], args[33], args[34], args[35], args[36], args[37], args[38], args[39],
            args[40], args[41], args[42], args[43], args[44], args[45], args[46], args[47],
            args[48], args[49], args[50], args[51], args[52], args[53], args[54], args[55],
            args[56], args[57], args[58], args[59], args[60], args[61], args[62], args[63],
            args[64], args[65], args[66], args[67], args[68], args[69], args[70], args[71],
            args[72], args[73], args[74], args[75], args[76], args[77], args[78], args[79],
            args[80], args[81], args[82], args[83], args[84], args[85], args[86], args[87],
            args[88], args[89], args[90], args[91], args[92], args[93], args[94], args[95],
            args[96], args[97], args[98], args[99], args[100], args[101], args[102], args[103],
            args[104], args[105], args[106], args[107], args[108], args[109], args[110], args[111],
            args[112], args[113], args[114], args[115], args[116], args[117], args[118], args[119],
            args[120], args[121], args[122], args[123], args[124], args[125], args[126], args[127]
        )
        else -> throw IllegalArgumentException(
            "To many `vararg` arguments. Maximum supported arguments: 128."
        )
    }
}