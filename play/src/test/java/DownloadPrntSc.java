import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DownloadPrntSc {

    private static final List<String> CODE_NRS = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
            , "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n"
            , "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");

    @Test
    public void download() throws IOException {
        String imageIdStart = "100001";
        List<String> imageIds = generateImageIds(imageIdStart, 2000);
        for(String imageId : imageIds) {
            String html = downloadHtml("https://prnt.sc/" + imageId);
            String imgurUrl = extractImgurUrl(html);
            if(imgurUrl == null){
                continue;
            }
            downloadImageFromUrl(imgurUrl, imageId);
        }
    }

    private List<String> generateImageIds(String startImageId, int total) {
        List<String> result = new ArrayList<>();
        List<String> digits = new ArrayList<>(Arrays.asList(startImageId.split("")));
        for(int i = 0; i < total; i++){
            String newCode = doPlusOneOnDigits(digits);
            result.add(newCode);
            updateDigitsList(digits, newCode);
        }
        return result;
    }

    private String doPlusOneOnDigits(List<String> digits) {
        int digitsSize = digits.size();
        if("z".equals(digits.get(digitsSize - 1))){
            List<String> digitsMinusLast = new ArrayList<>(digits);
            digitsMinusLast.remove(digitsSize - 1);
            return doPlusOneOnDigits(digitsMinusLast) + "0";
        }
        int codeIndexLast = CODE_NRS.indexOf(digits.get(digitsSize - 1));
        String newLastDigit = CODE_NRS.get(codeIndexLast + 1);
        digits.set(digitsSize - 1, newLastDigit);
        return StringUtil.join(digits, "");
    }

    private void updateDigitsList(List<String> digits, String result) {
        String[] newDigits = result.split("");
        for(int i = 0; i < 6; i++) {
            digits.set(i, newDigits[i]);
        }
    }

    private void downloadImageFromUrl(String imgurUrl, String imageId) throws IOException {
        URL url = new URL(imgurUrl);
        File destination_file = new File("D:\\prnt.sc\\" + imageId + ".png");
        FileUtils.copyURLToFile(url, destination_file);
    }

    private String extractImgurUrl(String html) {
        String[] lines = html.split("\n");
        for(String line : lines){
            if(line.contains("imgur")){
                return line.replaceFirst(".*(https.*.imgur.*png).*", "$1");
            }
        }
        return null;
    }

    private String downloadHtml(String url) throws IOException {
        return Jsoup.connect(url).get().html();
    }
}
