import tensorflow as tf
with tf.Session() as sess:
    saver = tf.train.import_meta_graph('/tmp/_retrain_checkpoint.meta')
    saver.restore(sess, "/tmp/_retrain_checkpoint.ckpt")
    saver.save(sess,'/tmp/seckpoint.ckpt')