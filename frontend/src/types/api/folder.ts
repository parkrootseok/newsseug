import { Article } from './article';

export interface MemberFolderInfo {
  id: number;
  name: string;
  articleCount: number;
  thumbnailUrl: string;
}

export interface MemberFolderList {
  folders: MemberFolderInfo[];
}
export interface Folder {
  articles: Article[];
  id: number;
  name: string;
}