package me.tigermouthbear.theia.checks

import me.tigermouthbear.theia.Possible
import me.tigermouthbear.theia.Program
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.TypeInsnNode

/**
 * @author Tigermouthbear
 * 4/11/20
 */

class ConnectionCheck: AbstractCheck("WebConnectionCheck") {
	private val types = arrayOf(
		"java/net/HttpURLConnection",
		"java/net/HttpsURLConnection",
		"org/apache/http/impl/client/CloseableHttpClient",
		"okhttp3/Request",
		"java/net/Socket",
		"java/net/InetSocketAddress",
		"org/apache/http/impl/client/HttpClientBuilder",
		"org/apache/http/client/methods/HttpPost"

	)

	private val methods = arrayOf(
		"java/net/URL:openConnection:()Ljava/net/URLConnection;",
		"java/net/URL:openStream:()Ljava/io/InputStream;"
	)

	override fun run(program: Program, path: String) {
		for(cn in program.getClassNodes().values) {
			if(!cn.name.startsWith(path)) continue
			for(mn in cn.methods) {
				for(insn in mn.instructions) {
					if(insn is TypeInsnNode && types.contains(insn.desc)) {
						possibles.add(
							Possible(
								Possible.Severity.WARN,
								"Found connection",
								cn.name
							)
						)
					} else if(insn is MethodInsnNode && methods.contains(format(insn))) {
						possibles.add(
							Possible(
								Possible.Severity.WARN,
								"Found connection",
								cn.name
							)
						)
					}
				}
			}
		}
	}
}