import I18n from 'react-native-i18n';
import en from './en';
import vn from './vn';

I18n.fallbacks = true;
I18n.locale = "en";
I18n.translations = {
  en, vn
};

export default I18n; 