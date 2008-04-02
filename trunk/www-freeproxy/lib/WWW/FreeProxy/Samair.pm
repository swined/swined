package WWW::FreeProxy::Samair;

use LWP::Simple;
use HTML::TreeBuilder;

sub fetch {
	my ($i, $b, $self, $callback) = (0, 1, @_);
	my %page = ('01'=>0);
	while ($b){
		$b = 0;
		foreach my $p(keys %page) {
			next if($page{$p});
			$page{$p} = 1;
			$b = 1;
			my $content = get("http://www.samair.ru/proxy/proxy-$p.htm") or next;
			$page{$1} ||= 0 while $content =~ /proxy-(\d{2})\.htm/g;
			my $h = HTML::TreeBuilder->new_from_content($content);
			$h->ignore_unknown(0);
			$h = $h->elementify();
			my $t = $h->look_down('_tag', 'table', 'class', 'tablelist');
			my @td = $t->look_down('_tag', 'td', sub {if($_[0]) {$_[0]->as_text() =~ /\d+\.\d+\.\d+\.\d+/}});
			foreach(@td) {
				my $a = $_->as_text();
				$a =~ s/\s+//g;
				&$callback($a);
			}
			$h = $h->delete();
		}
	}
}

1;
