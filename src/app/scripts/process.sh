mkdir $1;

ffmpeg \
  -i "$2" -b:a:0 64k -b:a:1 32k \
  -map 0:a -map 0:a \
  -hls_flags delete_segments \
  -hls_list_size 0 \
  -f hls -hls_time 10 \
  -strict -2 -hls_base_url "https://s3.us-east-2.amazonaws.com/kletka/p/$1/" \
  -hls_segment_filename "./$1/song-%05d.ts" \
  "index.m3u8" \
  -loglevel panic

for f in $1/*.ts; do bash ./src/app/scripts/upload.sh "$f" "kletka" "p" $3 $4; done

bash ./src/app/scripts/upload.sh "index.m3u8" "kletka" "p/$1" $3 $4;

rm -rf $1
rm -rf index.m3u8
