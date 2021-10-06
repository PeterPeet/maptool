/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package net.rptools.maptool.model.framework.dropinlibrary;

import com.google.protobuf.util.JsonFormat;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.filechooser.FileFilter;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.framework.proto.DropInLibraryDto;

public class DropInLibraryImporter {

  public static final String DROP_IN_LIBRARY_EXTENSION = ".mtlib";
  private static final String LIBRARY_INFO_FILE = "library.json";

  public static FileFilter getDropInLibraryFileFilter() {
    return new FileFilter() {

      @Override
      public boolean accept(File f) {
        return f.isDirectory() || f.getName().endsWith(DROP_IN_LIBRARY_EXTENSION);
      }

      @Override
      public String getDescription() {
        return I18N.getText("file.ext.dropInLib");
      }
    };
  }

  public DropInLibrary importFromFile(File file) throws IOException {
    var diiBuilder = DropInLibraryDto.newBuilder();
    try (var zip = new ZipFile(file)) {
      ZipEntry entry = zip.getEntry(LIBRARY_INFO_FILE);
      if (entry == null) {
        throw new IOException("library.json file not found.");
      }
      var builder = DropInLibraryDto.newBuilder();
      JsonFormat.parser().merge(new InputStreamReader(zip.getInputStream(entry)), builder);
      return DropInLibrary.fromDto(builder.build(), Map.of());
    }
  }
}
