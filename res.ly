\version "2.18.2"
\include "english.ly"
  #(set-global-staff-size 20) % mdpi
% #(set-global-staff-size 30) % hdpi
% #(set-global-staff-size 40) % xhdpi
% #(set-global-staff-size 60) % xxhdpi
% #(set-global-staff-size 80) % xxxhdpi
\score { {
  \clef treble
  c'''1 bf cs'' df'' <bf' fs''> <b' c''> <c'' c''> <cs'' cf''> \break
  \clef alto
  cs'' df fss' cff' <b d'> <gff eff'> <gss ess> <c' cs'> \break
  \clef bass
  fss' cff, c bf, <gff, fff> <bf, cs> <cs cs> <cf cff> \break
} }
