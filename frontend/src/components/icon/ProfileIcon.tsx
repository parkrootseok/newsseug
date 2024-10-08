import { useRef, useState } from 'react';
import { ProfileImageProps } from 'types/props/register';
import ProfileImageModal from 'components/register/ProfileImageModal';

function ProfileIcon({
  profileImageUrl,
  selectImage,
  saveImage,
  removeImage,
}: Readonly<ProfileImageProps>) {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const fileInputRef = useRef<HTMLInputElement | null>(null);
  const handleSelectImageClick = () => {
    fileInputRef.current?.click();
  };

  const handleImageAdjustClick = () => {
    setIsModalOpen(true);
  };

  const handleSaveImage = (newProfileImage: File) => {
    saveImage(newProfileImage);
    setIsModalOpen(false);
  };
  const handleRemoveImage = () => {
    removeImage();
    setIsModalOpen(false);
  };

  return (
    <div>
      <svg
        width="114"
        height="108"
        viewBox="0 0 114 108"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <circle
          cx="54"
          cy="54"
          r="53.25"
          fill={profileImageUrl ? `url(#profileImage)` : 'white'}
          onClick={handleImageAdjustClick}
          stroke="#D4D4D4"
          strokeWidth="1.5"
        />
        {!profileImageUrl && (
          <>
            <path
              d="M54 54C59.5228 54 64 49.5228 64 44C64 38.4772 59.5228 34 54 34C48.4772 34 44 38.4772 44 44C44 49.5228 48.4772 54 54 54Z"
              fill="#909090"
            />
            <path
              d="M42 56H66C72.624 56 78 61.376 78 68C78 71.312 75.312 74 72 74H36C32.688 74 30 71.312 30 68C30 61.376 35.376 56 42 56Z"
              fill="#909090"
            />
          </>
        )}

        <defs>
          <pattern
            id="profileImage"
            patternUnits="userSpaceOnUse"
            width="100%"
            height="100%"
          >
            {profileImageUrl && (
              <image
                href={profileImageUrl}
                x="0"
                y="0"
                width="108"
                height="108"
                preserveAspectRatio="xMidYMid slice"
              />
            )}
          </pattern>
        </defs>

        {/* 아래쪽 작은 원 */}
        <circle
          cx="96"
          cy="90"
          r="17.25"
          fill="#58D7A2"
          stroke="#D4D4D4"
          strokeWidth="1.5"
          onClick={handleSelectImageClick}
          style={{ cursor: 'pointer' }}
        />

        <path
          d="M102.5 86.5C103.325 86.5 104 87.175 104 88V95C104 95.825 103.325 96.5 102.5 96.5H89.5C88.675 96.5 88 95.825 88 95V88C88 87.175 88.675 86.5 89.5 86.5H102.5ZM102.5 85.5H89.5C88.12 85.5 87 86.62 87 88V95C87 96.38 88.12 97.5 89.5 97.5H102.5C103.88 97.5 105 96.38 105 95V88C105 86.62 103.88 85.5 102.5 85.5Z"
          fill="white"
        />
        <path
          d="M97.4062 83.4375C98.1797 83.4375 98.8125 84.0703 98.8125 84.8438V85.3125H93.1875V84.8438C93.1875 84.0703 93.8203 83.4375 94.5938 83.4375H97.4062ZM97.4062 82.5H94.5938C93.3 82.5 92.25 83.55 92.25 84.8438V86.25H99.75V84.8438C99.75 83.55 98.7 82.5 97.4062 82.5Z"
          fill="white"
        />
        <path
          d="M96 89.5C97.105 89.5 98 90.395 98 91.5C98 92.605 97.105 93.5 96 93.5C94.895 93.5 94 92.605 94 91.5C94 90.395 94.895 89.5 96 89.5ZM96 88.5C94.345 88.5 93 89.845 93 91.5C93 93.155 94.345 94.5 96 94.5C97.655 94.5 99 93.155 99 91.5C99 89.845 97.655 88.5 96 88.5Z"
          fill="white"
        />
      </svg>
      {/* {isModalOpen && profileImage && (
        <ProfileImageModal
          profileImage={profileImage}
          onClose={() => setIsModalOpen(false)}
          onSave={handleSaveImage}
          onRemove={handleRemoveImage}
        />
      )} */}
      <input
        type="file"
        accept="image/*"
        ref={fileInputRef}
        style={{ display: 'none' }}
        onChange={selectImage}
      />
    </div>
  );
}

export default ProfileIcon;
