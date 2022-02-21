package dod.utils

import java.io.File

object FileUtils {
    
    def filesInDir(dir: String, filterExtensions: Seq[String] = Seq.empty): Seq[File] = {
        val d = new File(dir)

        if (d.exists && d.isDirectory) {
            val extensions = filterExtensions.map(ext => "." + ext).toSet

            val files = d.listFiles
                .filter(_.isFile)
                .toSeq

            if (extensions.nonEmpty) {
                files.filter(file => extensions.exists(ext => file.getName.endsWith(ext)))
            } else {
                files
            }
        } else {
            Seq.empty
        }
    }

    def fileName(file: File): String = {
        val name = file.getName

        val i = name.lastIndexOf('.')

        if (i == -1) {
            name
        } else {
            name.substring(0, i)
        }
    }
}
