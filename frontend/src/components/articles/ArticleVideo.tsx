import { useRef, useState } from 'react';
import styled from 'styled-components';
import ArticleInfo from 'components/articles/ArticleInfo';
import ProgressBar from 'components/articles/ProgressBar';
import playIcon from 'assets/playIcon.svg';
import ArticleButtons from './ArticleButtons';

interface ArticleVideoProp {
  src: string;
}

function ArticleVideo({ src }: ArticleVideoProp) {
  const videoRef = useRef<HTMLVideoElement>(null);
  const [isPlaying, setIsPlaying] = useState(true);
  const [progress, setProgress] = useState(0);

  const togglePlay = () => {
    if (videoRef.current) {
      if (isPlaying) {
        videoRef.current.pause();
      } else {
        videoRef.current.play();
      }
      setIsPlaying(!isPlaying);
    }
  };

  const updateProgress = () => {
    if (videoRef.current) {
      const currentTime = videoRef.current.currentTime;
      const duration = videoRef.current.duration;
      setProgress(currentTime / duration);
    }
  };

  const handleSeek = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (videoRef.current) {
      const newTime =
        parseFloat(event.target.value) * videoRef.current.duration;
      videoRef.current.currentTime = newTime;
      setProgress(parseFloat(event.target.value));
    }
  };

  return (
    <Container>
      <VideoWrapper>
        <ShrotForm
          muted
          autoPlay
          playsInline
          loop
          src={src}
          ref={videoRef}
          onClick={togglePlay}
          onTimeUpdate={updateProgress}
        />
        {!isPlaying && (
          <PlayButton onClick={togglePlay}>
            <img src={playIcon} alt="play icon" />
          </PlayButton>
        )}
        {/* <ArticleButtonsWrapper>
        </ArticleButtonsWrapper> */}
        <ArticleButtons />
      </VideoWrapper>
      <ArticleContainer>
        <ArticleInfo />
        <ProgressBar
          progress={progress}
          isPlaying={isPlaying}
          onSeek={handleSeek}
        />
      </ArticleContainer>
    </Container>
  );
}

export default ArticleVideo;

const Container = styled.div`
  width: 100%;
  height: 100%;
  z-index: 1;
  position: relative;
  background-color: #000;
`;

const ShrotForm = styled.video`
  width: 100%;
  object-fit: cover;
`;

const VideoWrapper = styled.div`
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const ArticleContainer = styled.div`
  position: absolute;
  bottom: 0;
  width: 100%;
  background: linear-gradient(
    180deg,
    rgba(0, 0, 0, 0) 4.5%,
    rgba(0, 0, 0, 0.8) 68%,
    rgba(0, 0, 0, 0.9) 100%
  );
`;

const PlayButton = styled.button`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  border-radius: 50%;
  width: 60px;
  height: 60px;

  svg {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
`;