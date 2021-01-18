package io.yegair.jom

import okio.Buffer
import okio.ForwardingSource
import okio.Sink
import okio.Source
import java.io.IOException

internal class ReplicatingSource(delegate: Source, private val replication: Sink) : ForwardingSource(delegate) {

    /**
     * Used to cache reads until replication is confirmed.
     */
    private val readBuffer: Buffer = Buffer()

    /**
     * Used to copy reads for writing to the replication sink.
     */
    private val replBuffer: Buffer = Buffer()

    /**
     * State of the replication link.
     */
    private var stopped = false

    @Throws(IOException::class)
    override fun read(sink: Buffer, byteCount: Long): Long {
        // Read from source into read buffer.
        val result = super.read(readBuffer, byteCount)
        if (result == -1L) {
            stop() // stop replication
            return result
        }
        synchronized(replication) {
            if (!stopped) {
                // Copy read buffer and write to replication
                readBuffer.copyTo(replBuffer, 0, result)
                replication.write(replBuffer, result)
            }
        }

        // Confirm read by writing read buffer to read sink
        sink.write(readBuffer, result)
        return result
    }

    @Throws(IOException::class)
    override fun close() {
        stop() // stop replication
        super.close()
    }

    /**
     * Stop replicating data, non-reversible.
     */
    @Throws(IOException::class)
    private fun stop() {
        synchronized(replication) {
            stopped = true
            replication.close()
        }
    }

//    /**
//     * Returns a wrapped source which will stop replication when closed.
//     */
//    fun stopOnClose(source: Source): Source {
//        return object : ForwardingSource(source) {
//            @Throws(IOException::class)
//            override fun close() {
//                stop()
//                super.close()
//            }
//        }
//    }
}
