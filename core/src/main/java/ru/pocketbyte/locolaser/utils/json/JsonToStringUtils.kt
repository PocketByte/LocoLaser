package ru.pocketbyte.locolaser.utils.json

import org.json.simple.JSONAware

object JsonToStringUtils {

    public fun toJSONString(aware: JSONAware?): String? {
        return toJSONString(aware, 2)
    }

    public fun toJSONString(aware: JSONAware?, indent: Int): String? {
        return toJSONString(aware, indent, 0)
    }

    private fun toJSONString(aware: JSONAware?, indent: Int, space: Int): String? {
        return when (aware) {
            is Map<*, *> -> toJSONString(aware as Map<Any?, Any?>, indent, space)
            is List<*> -> toJSONString(aware as List<Any?>, indent, space)
            else -> null
        }
    }

    private fun toJSONString(list: List<Any?>?, indent: Int, space: Int): String {
        if (list == null)
            return "null"

        var first = true
        val sb = StringBuffer()
        val iter = list.iterator()

        sb.append('[')
        sb.appendNewLine(indent, space)
        while (iter.hasNext()) {
            if (first) {
                first = false
                sb.appendIndent(indent, space)
            } else {
                sb.append(',')
                sb.appendNewLine(indent, space)
                sb.appendIndent(indent, space)
            }

            val value = iter.next()
            if (value == null) {
                sb.append("null")
                continue
            }
            sb.append(valueToJSONString(value, indent, space + indent))
        }
        sb.appendNewLine(indent, space)
        sb.append(']')
        return sb.toString()
    }

    private fun toJSONString(map: Map<Any?, Any?>?, indent: Int, space: Int): String {
        if (map == null)
            return "null"

        val sb = StringBuffer()
        var first = true
        val iter = map.entries.iterator()

        sb.append('{')
        sb.appendNewLine(indent, space)

        while (iter.hasNext()) {
            if (first) {
                first = false
                sb.appendIndent(indent, space)
            } else {
                sb.append(',')
                sb.appendNewLine(indent, space)
                sb.appendIndent(indent, space)
            }

            (iter.next() as Map.Entry<*, *>).let { entry ->
                toJSONString(entry.key.toString(), entry.value, sb, indent, space + indent)
            }
        }
        sb.appendNewLine(indent, space)
        sb.append('}')
        return sb.toString()
    }


    private fun toJSONString(key: String?, value: Any?, sb: StringBuffer, indent: Int, space: Int): String {
        sb.append('\"')
        if (key == null)
            sb.append("null")
        else
            escape(key, sb)
        sb.append("\": ")

        sb.append(valueToJSONString(value, indent, space))

        return sb.toString()
    }

    private fun valueToJSONString(value: Any?, indent: Int, space: Int): String? {
        return when (value) {
            null -> "null"
            is String -> "\"" + escape(value) + "\""
            is Double -> if (value.isInfinite() || value.isNaN()) "null" else value.toString()
            is Float -> if (value.isInfinite() || value.isNaN()) "null" else value.toString()
            is Number -> value.toString()
            is Boolean -> value.toString()
            is JSONAware -> toJSONString(value, indent, space + indent)
            is Map<*, *> -> toJSONString(value as Map<Any?, Any?>, indent, space + indent)
            is List<*> -> toJSONString(value as List<*>?, indent, space + indent)
            else -> value.toString()
        }
    }

    private fun escape(s: String?): String? {
        if (s == null)
            return null
        val sb = StringBuffer()
        escape(s, sb)
        return sb.toString()
    }

    private fun escape(s: String, sb: StringBuffer) {
        for (i in 0 until s.length) {
            val ch = s[i]
            when (ch) {
                '"' -> sb.append("\\\"")
                '\\' -> sb.append("\\\\")
                '\b' -> sb.append("\\b")
                '\u000C' -> sb.append("\\f")
                '\n' -> sb.append("\\n")
                '\r' -> sb.append("\\r")
                '\t' -> sb.append("\\t")
                '/' -> sb.append("\\/")
                else ->
                    //Reference: http://www.unicode.org/versions/Unicode5.1.0/
                    if (ch in '\u0000'..'\u001F' || ch in '\u007F'..'\u009F' || ch in '\u2000'..'\u20FF') {
                        val ss = Integer.toHexString(ch.toInt())
                        sb.append("\\u")
                        for (k in 0 until 4 - ss.length) {
                            sb.append('0')
                        }
                        sb.append(ss.toUpperCase())
                    } else {
                        sb.append(ch)
                    }
            }
        }//for
    }
}

private fun StringBuffer.appendNewLine(indent: Int, space: Int) {
    if (indent < 0)
        return

    append('\n')
    for (i in 0 until space)
        append(' ')
}

private fun StringBuffer.appendIndent(indent: Int, space: Int) {
    if (indent < 0)
        return

    for (i in 0 until indent)
        append(' ')
}

private fun StringBuffer.appendSpace(indent: Int, space: Int) {
    if (indent < 0)
        return

    for (i in 0 until space)
        append(' ')
}