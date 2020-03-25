import tensorflow as tf
import numpy as np
from inception.data import build_image_data
from inception import image_processing
from inception import inception_model as inception
from os import listdir
from os.path import isfile, join

# 코드 참고
# https://inyl.github.io/programming/2017/08/11/cnn_image_search.html
# 2020.01.14 화요일 tensorflow inception-v3 활용 이미지 트레이닝
# 홈페이지 python 2 버전이니 적절한 수정 필요

check_point_dir = "inception-v3"
batch_size = 100
my_image_path = "/Users/parkbyungnam/OneDrive - kpu.ac.kr/MyWorkSpace/VScode_WORKSPACE/CJP/images/"
img_file_list = [f for f in listdir(my_image_path) if (f.rfind('jpg') > -1)]
file_size = len(img_file_list)

def inference_on_multi_image():
  print("total image size {} ".format(file_size) )
  total_batch_size = file_size // batch_size + 1
  logit_list = []

  for n in range(total_batch_size):
      print("step :{} / {}".format(n + 1, total_batch_size))
      mini_batch = img_file_list[n * batch_size: (n + 1) * batch_size]
      mini_adarr = np.ndarray(shape=(0, 299,299,3))
        
      with tf.Graph().as_default():
        num_classes = 1001 # (logit size)

        coder = build_image_data.ImageCoder()
        for i, image in enumerate(mini_batch):
          image_buffer, _, _ =  build_image_data._process_image(my_image_path + image, coder)
          image = image_processing.image_preprocessing(image_buffer, 0, False) # image -> (299, 299, 3)
          image = tf.expand_dims(image, 0) # (299, 299,3) -> (1, 299, 299, 3)
          mini_adarr = tf.concat([mini_adarr, image], 0) 

        logits, _ = inception.inference(mini_adarr, num_classes, for_training=False, restore_logits=True)

        saver = tf.train.Saver()
        with tf.Session() as tf_session:
          ckpt = tf.train.get_checkpoint_state(checkpoint_dir)
          if ckpt and ckpt.model_checkpoint_path:
            if os.path.isabs(ckpt.model_checkpoint_path):
              # Restores from checkpoint with absolute path.
              saver.restore(tf_session, ckpt.model_checkpoint_path)
            else:
              # Restores from checkpoint with relative path.
              saver.restore(tf_session, os.path.join(checkpoint_dir,
                                               ckpt.model_checkpoint_path))

          l = tf_session.run([logits])
          for row in l[0]:
            logit_list.append(row)
                
  return logit_list

logit_list = inference_on_multi_image()

