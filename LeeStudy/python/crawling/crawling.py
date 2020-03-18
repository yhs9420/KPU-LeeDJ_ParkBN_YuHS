"""
from google_images_download import google_images_download
import ssl  # ssl Error 발생 시

ssl._create_default_https_context = ssl._create_unverified_context



def imageCrawling(keyword, dir):
    response = google_images_download.googleimagesdownload()

    arguments = {"keywords": keyword,  # 검색 키워드
                 "limit": 1000,  # 크롤링 이미지 수
                 "print_urls": True,  # 이미지 url 출력
                 "no_directory": True,  #
                 "chromedriver": "C:/Users/LG/Desktop/kpu_project/chromedriver_win32/chromedriver",
                 'output_directory': dir}  # 크롤링 이미지를 저장할 폴더

    paths = response.download(arguments)
    print(paths)


imageCrawling('onion', 'C:/Users/LG/Desktop/deep/ingredient/onion')
"""

from icrawler.builtin import GoogleImageCrawler,BaiduImageCrawler, BingImageCrawler
"""
google_crawler = GoogleImageCrawler(
    feeder_threads=1,
    parser_threads=1,
    downloader_threads=4,
    storage={'root_dir': 'C:/Users/LG/Desktop/deep/ingredient/so'})

filters = dict(
    size='large',
    #color='orange',
    license='commercial,modify',
    date=((2019, 1, 1), (2019, 11, 30)))
google_crawler.crawl(keyword='onion', filters=filters, offset=0, max_num=1000,
                     min_size=(200,200), max_size=None, file_idx_offset=0)
"""

bing_crawler = BingImageCrawler(downloader_threads=4,
                                storage={'root_dir': 'C:/Users/LG/Desktop/deep/ingredient/onion'})
bing_crawler.crawl(keyword='onion', filters=None, offset=0, max_num=1000)

"""
baidu_crawler = BaiduImageCrawler(storage={'root_dir': 'your_image_dir'})
baidu_crawler.crawl(keyword='cat', offset=0, max_num=1000,
                    min_size=(200,200), max_size=None)
"""
