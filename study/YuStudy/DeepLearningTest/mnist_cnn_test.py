# 학습한 모델 테스트
from keras.models import load_model
from matplotlib import pyplot as plt

model = load_model('mnist_cnn_00.h5')
model.summary()

test_num = plt.imread('./3.JPG')
test_num = test_num[:, :, 0]
test_num = (test_num > 125) * test_num
test_num = test_num.astype('float32') / 255.

plt.imshow(test_num, cmap='Greys', interpolation='nearest')

test_num = test_num.reshape((1, 28, 28, 1))
print('The Answer is ', model.predict_classes(test_num))
plt.show()
